package com.appachhi.plugin.instrumentation.annotation;

public class AppachhiMethodTraceConfig extends AnnotatedMethodInstrumentationConfig {
    public AppachhiMethodTraceConfig() {
        super(new AppachhiMethodTraceProcessor.Factory(), "Lcom/appachhi/sdk/instrument/trace/Trace;");
    }
}
