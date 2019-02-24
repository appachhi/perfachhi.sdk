package com.appachhi.plugin.instrumentation.annotation;

public interface AnnotatedMethodAdapter {
    void onMethodEnter();

    void onMethodExit();
}

