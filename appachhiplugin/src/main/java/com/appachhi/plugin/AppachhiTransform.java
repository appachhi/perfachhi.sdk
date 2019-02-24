package com.appachhi.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent.ContentType;
import com.android.build.api.transform.QualifiedContent.DefaultContentType;
import com.android.build.api.transform.QualifiedContent.Scope;
import com.android.build.api.transform.Status;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.AppExtension;
import com.appachhi.plugin.instrumentation.Instrument;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Transformation for Appachhi SDK  supporting
 * <ul>Automatic Screen Transition for Activity</ul>
 * <ul>Method Call Tracing</ul>
 */
public class AppachhiTransform extends Transform {
    private static final String NAME = "AppachhiSDKPlugin";
    private final Logger L = Logging.getLogger(NAME);
    private Set<ContentType> inputTypes = Sets.newHashSet(new ContentType[]{DefaultContentType.CLASSES});
    private Set<Scope> scopes = ImmutableSet.of(Scope.PROJECT, Scope.EXTERNAL_LIBRARIES, Scope.SUB_PROJECTS);
    @SuppressWarnings("FieldCanBeLocal")
    private Project project;
    @SuppressWarnings("FieldCanBeLocal")
    private Instrument instrument;
    private AppExtension appExtension;

    AppachhiTransform(Project project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Set<ContentType> getInputTypes() {
        return inputTypes;
    }

    @Override
    public Set<? super Scope> getScopes() {
        return scopes;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {
        L.debug("Transformation Begin");
        appExtension = (AppExtension) project.getExtensions().findByName("android");
        Collection<TransformInput> inputs = invocation.getInputs();
        Collection<TransformInput> referencedInputs = invocation.getReferencedInputs();
        TransformOutputProvider outputProvider = invocation.getOutputProvider();

        // Create File URL from the inputs
        List<URL> classPathURLs = getClassPathUrls(inputs, referencedInputs);
        L.debug("Fetched all File URL");

        // Creates classloader with the created file url
        ClassLoader classLoader = new URLClassLoader(classPathURLs.toArray(new URL[0]), null);
        L.debug("Created classloader from URLs");

        if (!isIncremental()) {
            // Delete all the transformed files if this transformation doesnot support incremental
            // build
            outputProvider.deleteAll();
        }
        // Create Instrument Instance
        instrument = new Instrument(classLoader, "com.appachhi.sample");

        for (TransformInput input : inputs) {
            // Transform the files from directory
            transformDirectoryInputs(input, instrument, outputProvider);
            // Transform the files from jar
            transformJarInputs(input, instrument, outputProvider);
        }

    }

    /**
     * Transform Files from Directory which is same as classes from own project or modules
     *
     * @param input          Input
     * @param instrument     {@link Instrument}
     * @param outputProvider {@link TransformOutputProvider}
     * @throws IOException throw when it failed to instrument the classes
     */
    private void transformDirectoryInputs(TransformInput input,
                                          Instrument instrument,
                                          TransformOutputProvider outputProvider) throws IOException {
        L.debug("Transform Directory Input");
        for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
            L.debug(String.format("Input File Path : %s", directoryInput.getFile().getAbsolutePath()));
            // Create Output file directory where transformed files will be kept
            String folderName = DigestUtils.md5Hex(directoryInput.getFile().getName());
            File outputDir = outputProvider.getContentLocation(
                    folderName,
                    directoryInput.getContentTypes(),
                    directoryInput.getScopes(),
                    Format.DIRECTORY);
            L.debug(String.format("Output directory for directory input %s", outputDir.getAbsolutePath()));
            // Send File for Instrument
            instrument.instrumentClassesInDirectory(directoryInput.getFile(), 0, outputDir);
        }
    }

    /**
     * Transform Files from JAR which is same as external dependency class files
     *
     * @param input          Input
     * @param instrument     {@link Instrument}
     * @param outputProvider {@link TransformOutputProvider}
     * @throws IOException throw when it failed to instrument the classes
     */
    private void transformJarInputs(TransformInput input,
                                    Instrument instrument,
                                    TransformOutputProvider outputProvider) throws IOException {
        for (JarInput jarInput : input.getJarInputs()) {
            // Location of Jar File
            File jar = jarInput.getFile();
            String jarName = jar.getName();
            int lastIndex = jarName.lastIndexOf(".");
            String folderName;
            String jarFilePathHex = DigestUtils.md5Hex(jar.getPath());
            if (lastIndex != -1) {
                String jarLastName = jarName.substring(0, lastIndex);
                folderName = String.format(Locale.ENGLISH, "1%d%d%s-%s", jarFilePathHex.length(), jarLastName.length(), jarFilePathHex, jarName);
            }else {
                folderName = String.format(Locale.ENGLISH, "1%d%d%s%s", jarFilePathHex.length(), jarName.length(), jarFilePathHex, jarName);
            }

            L.debug("Transform Input : %s", jarInput);
            // Create OutFile Where the transformed jar file will be kept
            File outputDir = outputProvider.getContentLocation(
                    folderName,
                    jarInput.getContentTypes(),
                    jarInput.getScopes(),
                    Format.DIRECTORY);
            // Return if the new directory can't be created
            if (!outputDir.mkdirs()) {
                L.error(String.format("Couldn't create directory %s", outputDir.getAbsolutePath()));
                return;
            }
            L.debug(String.format("Output directory created for %s : %s", jarName,
                    outputDir.getAbsolutePath()));
            if (jarInput.getStatus() == Status.REMOVED) {
                // Deletes the already existing file if the jar dependacy has been removed
                FileUtils.deleteQuietly(outputDir);
            }
            // Send File from JAR for Instrument
            instrument.instrumentClassesInJar(jar, outputDir);

        }
    }

    /**
     * Coverts all the {@link TransformInput} into URL so that {@link URLClassLoader}
     * can use it to load files
     *
     * @param transformInputs  Input which can be transformed
     * @param referencedInputs Referenced input which can be read only
     * @return List of URL
     */
    private List<URL> getClassPathUrls(Collection<TransformInput> transformInputs,
                                       Collection<TransformInput> referencedInputs) {
        List<File> files = new ArrayList<>(appExtension.getBootClasspath());
        for (Collection<TransformInput> inputs : Arrays.asList(transformInputs, referencedInputs)) {
            for (TransformInput input : inputs) {
                for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                    files.add(directoryInput.getFile());
                }
                for (JarInput jarInput : input.getJarInputs()) {
                    files.add(jarInput.getFile());
                }
            }
        }

        return Lists.transform(files, new Function<File, URL>() {
            @Override
            public URL apply(File file) {
                try {
                    return file.toURI().toURL();
                } catch (MalformedURLException e) {
                    L.error(String.format("Failed to transform because file %s", file.getAbsolutePath()));
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
