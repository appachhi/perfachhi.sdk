package com.appachhi.plugin.instrumentation.network;

public interface NetworkObjectInstrumentationFactory {
    NetworkObjectInstrumentation newObjectInstrumentation(String className, String methodName, String methodDesc);
}
