package com.appachhi.plugin.instrumentation;

import com.appachhi.plugin.instrumentation.annotation.AnnotatedMethodAdapter;
import com.appachhi.plugin.instrumentation.annotation.AnnotatedMethodInstrumentationFactory;
import com.appachhi.plugin.instrumentation.annotation.AppachhiMethodTraceProcessor;
import com.appachhi.plugin.instrumentation.model.AnnotationInfo;
import com.appachhi.plugin.instrumentation.model.ClassInfo;
import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentation;
import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationFactory;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.ArrayList;
import java.util.List;

class InstrumentationVisitor extends ClassVisitor {
    private ClassInfo classInfo = new ClassInfo();
    private InstrumentationConfig config;
    private final InstrumentationContext instrumentationContext = new InstrumentationContext();
    private String appPackageName;

    InstrumentationVisitor(ClassVisitor cv, InstrumentationConfig instrumentationConfig, String packageName) {
        super(Opcodes.ASM5, cv);
        config = instrumentationConfig;
        this.appPackageName = packageName;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName,
                      String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        classInfo.setType(Type.getObjectType(name));
        classInfo.setInterfaces(interfaces);
        classInfo.setSuperclass(superName);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationInfo annotationInfo = new AnnotationInfo();
        annotationInfo.setType(Type.getType(desc));
        classInfo.getAnnotations().add(annotationInfo);
        AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
        return new InstrumentationAnnotationVisitor(this.api, annotationVisitor, annotationInfo);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                     String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        boolean shouldTrace = this.appPackageName!=null && classInfo.getType().getClassName().startsWith(this.appPackageName);
        return new InstrumentationMethodVisitor(this.classInfo.getType().getDescriptor(),
                api, mv, access, name, desc, this.config, shouldTrace);
    }

    private static class InstrumentationAnnotationVisitor extends AnnotationVisitor {
        private AnnotationInfo annotationInfo;

        InstrumentationAnnotationVisitor(int api, AnnotationVisitor av, AnnotationInfo annotationInfo) {
            super(api, av);
            this.annotationInfo = annotationInfo;
        }

        @Override
        public void visit(String name, Object value) {
            annotationInfo.getValues().put(name, value);
            super.visit(name, value);
        }
    }

    private class InstrumentationMethodVisitor extends AdviceAdapter {

        private String classDesc;
        private String methodName;
        private String methodDesc;
        private InstrumentationConfig config;
        private List<AnnotatedMethodAdapter> annotatedMethodAdapters = new ArrayList<>();
        private AppachhiMethodTraceProcessor methodTraceProcessor;

        InstrumentationMethodVisitor(final String classDesc, final int api,
                                     final MethodVisitor mv, final int access,
                                     final String methodName, final String methodDesc,
                                     final InstrumentationConfig config) {
            super(api, mv, access, methodName, methodDesc);
            this.classDesc = classDesc;
            this.methodName = methodName;
            this.methodDesc = methodDesc;
            this.config = config;
        }

        InstrumentationMethodVisitor(final String classDesc, final int api,
                                     final MethodVisitor mv, final int access,
                                     final String methodName, final String methodDesc,
                                     final InstrumentationConfig config, boolean traceMethod) {
            this(classDesc, api, mv, access, methodName, methodDesc, config);
            if (traceMethod) {
                methodTraceProcessor = new AppachhiMethodTraceProcessor(this, classDesc, methodName);
            }
        }

        @Override
        public AnnotationVisitor visitAnnotationDefault() {
            AnnotationVisitor annotationVisitor = super.visitAnnotationDefault();
            AnnotationInfo annotationInfo = new AnnotationInfo();
            return new InstrumentationAnnotationVisitor(api, annotationVisitor, annotationInfo);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            AnnotationVisitor annotationVisitor = super.visitAnnotation(classDesc, visible);
            AnnotationInfo annotationInfo = new AnnotationInfo();
            AnnotationVisitor av = new InstrumentationAnnotationVisitor(api, annotationVisitor, annotationInfo);
            List<AnnotatedMethodInstrumentationFactory> factories = config.getAnnotatedMethodInstrumentationFactories(classDesc);
            if (factories != null) {
                for (AnnotatedMethodInstrumentationFactory fac : factories) {
                    annotatedMethodAdapters.add(fac.newAnnotatedMethodInstrumentation(
                            instrumentationContext, this, annotationInfo, methodName, methodDesc));
                }
            }

            return av;
        }

        protected void onMethodEnter() {
            super.onMethodEnter();
            for (AnnotatedMethodAdapter annotatedMethodAdapter : this.annotatedMethodAdapters) {
                annotatedMethodAdapter.onMethodEnter();
            }
            if (methodTraceProcessor != null) {
                methodTraceProcessor.onMethodEnter();
            }
        }

        protected void onMethodExit(final int opcode) {
            super.onMethodExit(opcode);
            for (AnnotatedMethodAdapter annotatedMethodAdapter : this.annotatedMethodAdapters) {
                annotatedMethodAdapter.onMethodExit();
            }

            if (methodTraceProcessor != null) {
                methodTraceProcessor.onMethodExit();
            }
        }

        public void visitMethodInsn(final int opcode, final String owner, final String name,
                                    final String desc, final boolean itf) {
            NetworkObjectInstrumentationFactory factory = this.config.getNetworkObjectInstrumentationFactory(owner, name, desc);
            NetworkObjectInstrumentation networkObjectInstrumentation = null;
            if (factory != null) {
                networkObjectInstrumentation = factory.newObjectInstrumentation(owner, name, desc);
            }
            if (networkObjectInstrumentation != null) {
                networkObjectInstrumentation.injectBefore(this.mv);
                if (!networkObjectInstrumentation.replaceMethod(this.mv, opcode)) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                }
                networkObjectInstrumentation.injectAfter(this.mv);
            } else {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }

        }
    }
}
