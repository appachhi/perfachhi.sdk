package com.appachhi.plugin.instrumentation.annotation;

import com.appachhi.plugin.instrumentation.model.AnnotationInfo;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

public class AppachhiMethodTraceProcessor implements AnnotatedMethodAdapter {
    private static final String TRACE_ANNOTATION_ATTR_NAME = "name";
    private static final String TRACE_ANNOTATION_ATTR_ENABLED = "enabled";
    private static final Type kclass = Type.getType("com/appachhi/sdk/instrument/trace/MethodTrace");
    private AnnotationInfo annotationInfo;
    private AdviceAdapter adviceAdapter;
    private int localIndex = -1;
    private boolean traceStarted = false;

    private AppachhiMethodTraceProcessor(AdviceAdapter adviceAdapter, AnnotationInfo annotationInfo) {
        this.adviceAdapter = adviceAdapter;
        this.annotationInfo = annotationInfo;
    }

    @Override
    public void onMethodEnter() {
        Object enabled = this.annotationInfo.values.get(TRACE_ANNOTATION_ATTR_ENABLED);
        boolean shouldTrace = true;
        //noinspection ConditionCoveredByFurtherCondition
        if (enabled != null && enabled instanceof Boolean) {
            shouldTrace = (Boolean) enabled;
        }
        try {
            if (shouldTrace) {
                startTrace();
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    private void startTrace() throws NoSuchMethodException {
        String traceName = annotationInfo.values.get(TRACE_ANNOTATION_ATTR_NAME).toString();
        this.localIndex = this.adviceAdapter.newLocal(kclass);
        this.adviceAdapter.push(traceName);
        this.adviceAdapter.invokeStatic(Type.getType("com/appachhi/sdk/Appachhi"), new Method("newTrace", "(Ljava/lang/String;)Lcom/appachhi/sdk/instrument/trace/MethodTrace;"));
        this.adviceAdapter.storeLocal(this.localIndex);
        traceStarted = true;
    }

    @Override
    public void onMethodExit() {
        try {
            if (traceStarted) {
                endTrace();
            }

        } catch (NoSuchMethodException var2) {
            throw new RuntimeException(var2);
        }
    }

    private void endTrace() throws NoSuchMethodException {
        if (this.localIndex == -1) {
            throw new IllegalStateException("Tracing is not started");
        } else {
            this.adviceAdapter.loadLocal(this.localIndex);
            Method stop = new Method("stop", "()V");
            this.adviceAdapter.invokeVirtual(kclass, stop);
        }
    }

    public static class Factory implements AnnotatedMethodInstrumentationFactory {
        public Factory() {
        }

        public AnnotatedMethodAdapter newAnnotatedMethodInstrumentation(AdviceAdapter mv, AnnotationInfo ai, String methodName, String methodDesc) {
            return new AppachhiMethodTraceProcessor(mv, ai);
        }
    }
}
