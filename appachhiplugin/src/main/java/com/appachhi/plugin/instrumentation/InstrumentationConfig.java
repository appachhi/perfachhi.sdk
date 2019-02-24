package com.appachhi.plugin.instrumentation;

import com.appachhi.plugin.instrumentation.annotation.AnnotatedMethodInstrumentationFactory;
import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationFactory;

import java.util.List;

import javax.annotation.Nullable;

public interface InstrumentationConfig {
    @Nullable
    List<AnnotatedMethodInstrumentationFactory> getAnnotatedMethodInstrumentationFactories(String classDesc);

    @Nullable
    NetworkObjectInstrumentationFactory getNetworkObjectInstrumentationFactory(String className, String methodName, String methodDesc);

    ClassLoader getClassLoader();
}
