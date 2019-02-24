package com.appachhi.plugin.instrumentation.annotation;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

public class AppachhiMethodTraceProcessor implements AnnotatedMethodAdapter {
    private static final Type kclass = Type.getType("com/appachhi/sdk/instrument/trace/MethodTrace");
    private AdviceAdapter adviceAdapter;
    private String className;
    private String methodName;
    private int localIndex = -1;

    public AppachhiMethodTraceProcessor(AdviceAdapter adviceAdapter, String className, String methodName) {
        this.adviceAdapter = adviceAdapter;
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public void onMethodEnter() {
        this.localIndex = this.adviceAdapter.newLocal(kclass);
        this.adviceAdapter.push(className + methodName);
        this.adviceAdapter.invokeStatic(Type.getType("com/appachhi/sdk/Appachhi"), new Method("startMethodTrace", "(Ljava/lang/String;)Lcom/appachhi/sdk/instrument/trace/MethodTrace;"));
        this.adviceAdapter.storeLocal(this.localIndex);
    }

    @Override
    public void onMethodExit() {
        if (this.localIndex == -1) {
            throw new IllegalStateException("Tracing is not started");
        } else {
            this.adviceAdapter.loadLocal(this.localIndex);
            Method stop = new Method("stop", "()V");
            this.adviceAdapter.invokeVirtual(kclass, stop);
        }
    }
}
