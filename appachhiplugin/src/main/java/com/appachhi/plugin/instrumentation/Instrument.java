package com.appachhi.plugin.instrumentation;

import com.appachhi.plugin.instrumentation.execption.AlreadyInstrumentedException;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

import org.apache.commons.io.FileUtils;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Class responsible for managing all the instrumentation done by the plugin
 */
public class Instrument {
    private final Logger L = Logging.getLogger("AppachhiSDKPlugin");
    private InstrumentationConfig instrumentationConfig;
    private String packageName;

    public Instrument(ClassLoader classLoader, String packageName) {
        instrumentationConfig = new InstrumentationConfigFactory(classLoader).newConfig();
        this.packageName = packageName;
    }

    /**
     * Instrument all the classes within a directory recursively
     *
     * @param dir       Base Directory
     * @param depth     Depth
     * @param outputDir Output File for the instrumented class
     * @throws IOException when cannot read file
     */
    public void instrumentClassesInDirectory(File dir, int depth, File outputDir) throws IOException {
        // Fetch all the file name in the dir directory
        String[] fileNames = dir.list();
        if (fileNames == null) {
            // This can happen unexpectedly
            throw new IOException("Unexpected empty array");
        }
        for (String fileName : fileNames) {
            // If file name is android and it is the first a node,then it means this is android code
            // Don't perform transformation and just copy it to output folder
            if (depth == 0 && fileName.equals("android")) {
                L.debug("Copying root android folder");
                FileUtils.copyDirectory(dir, outputDir);
            } else {
                File file = new File(dir, fileName);
                File outputFile = new File(outputDir, file.getName());
                // If the file is an directory, enter the folder and perform the operation
                // recursively
                if (file.isDirectory()) {
                    ++depth;
                    instrumentClassesInDirectory(file, depth, outputFile);
                } else if (fileName.endsWith(".class")) {
                    // This is class file which can be transformed
                    // Instrument it
                    L.debug(String.format("Instrument %s", fileName));
                    instrumentClassFile(file, outputFile);
                }
            }
        }
    }

    /**
     * Instrument the class file and write it back to the output file
     *
     * @param file       Input class file
     * @param outputFile Output file where instrumented code is writtem back
     * @throws IOException when File cannot be opened
     */
    private void instrumentClassFile(File file, File outputFile) throws IOException {
        L.debug(String.format("Instrumenting class file %s", file.getName()));
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        // Read Bytes from the class file into  byte array
        //noinspection UnstableApiUsage
        byte[] fileBytes = ByteStreams.toByteArray(is);
        is.close();
        //noinspection ResultOfMethodCallIgnored
        outputFile.getParentFile().mkdirs();
        //noinspection ResultOfMethodCallIgnored
        outputFile.createNewFile();

        try {
            // Instrument the byte array
            byte[] out = this.instrument(fileBytes);
            FileOutputStream fos = new FileOutputStream(outputFile);
            // Write back the instrument byte array to the output file
            fos.write(out);
            fos.close();
        } catch (AlreadyInstrumentedException e) {
            L.error(String.format("Can't instrument %s", file), e);
            // Write back the original file in case the instrumentation fails.It is very important
            // because if we don't write it back this will no be part of the apk
            //noinspection UnstableApiUsage
            Files.copy(file, outputFile);
        }

    }

    /**
     * Instrument all the classes within a JAR files
     *
     * @param jar       Location fo Jar File
     * @param outputDir Output folder where instrumented code is written back
     * @throws IOException when jar file cannot be opened
     */
    public void instrumentClassesInJar(File jar, File outputDir) throws IOException {
        JarInputStream jis = new JarInputStream(new BufferedInputStream(new FileInputStream(jar)));

        // JAR Entry is  file
        JarEntry entry;
        while ((entry = jis.getNextJarEntry()) != null) {
            String name = entry.getName();
            // Copy the content into a byte array
            //noinspection UnstableApiUsage
            byte[] entryBytes = ByteStreams.toByteArray(jis);
            jis.closeEntry();
            // Check if the file is of type class and it can be instrumented
            if (name.endsWith(".class") && checkIfInstrumentable(name)) {
                L.debug("Instrumenting %s", name);

                try {
                    entryBytes = this.instrument(entryBytes);
                } catch (Exception e) {
                    L.error("Can't instrument", e);
                }
            } else {
                L.debug("Not instrumenting " + name);
            }

            if (!entry.isDirectory()) {
                File outFile = new File(outputDir, name);
                //noinspection ResultOfMethodCallIgnored
                outFile.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                outFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(outFile);
                fos.write(entryBytes);
                fos.close();
            }
        }

        jis.close();
    }

    private byte[] instrument(byte[] classBytes) {
        ClassReader classReader = new ClassReader(classBytes);
        ClassWriter classWriter = new CustomClassWriter(3);
        InstrumentationVisitor instrumentationVisitor = new InstrumentationVisitor(classWriter, instrumentationConfig,this.packageName);
        classReader.accept(instrumentationVisitor, 4);
        return classWriter.toByteArray();
    }

    private static boolean checkIfInstrumentable(String name) {
        return !name.equals("android") && !name.startsWith("android/") &&
                !name.startsWith("kotlinx/") && !name.startsWith("kotlin/") &&
                !name.startsWith("androidx/") && !name.startsWith("com/google/android/material/") &&
                !name.startsWith("com/google/android/apps/common/proguard/") &&
                !name.startsWith("com/google/android/gms/") &&
                !name.startsWith("com/google/common") &&
                !name.startsWith("okhttp3/") &&
                !name.startsWith("com/squareup/okhttp") &&
                !name.startsWith("okio/") &&
                !name.startsWith("io/reactivex/") &&
                !name.startsWith("com/appachhi/sdk") &&
                !name.startsWith("com/squareup/leakcanary");
    }

    private class CustomClassWriter extends ClassWriter {
        CustomClassWriter(final int flags) {
            super(flags);
        }

        protected String getCommonSuperClass(final String type1, final String type2) {
            ClassLoader classLoader = instrumentationConfig.getClassLoader();

            Class c;
            Class d;
            try {
                c = Class.forName(type1.replace('/', '.'), false, classLoader);
                d = Class.forName(type2.replace('/', '.'), false, classLoader);
            } catch (Throwable var7) {
                L.warn(var7.toString());
                return "java/lang/Object";
            }

            //noinspection unchecked
            if (c.isAssignableFrom(d)) {
                return type1;
            } else //noinspection unchecked
                if (d.isAssignableFrom(c)) {
                    return type2;
                } else if (!c.isInterface() && !d.isInterface()) {
                    //noinspection unchecked
                    do {
                        c = c.getSuperclass();
                    } while (!c.isAssignableFrom(d));

                    return c.getName().replace('.', '/');
                } else {
                    return "java/lang/Object";
                }
        }
    }
}
