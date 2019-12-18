package com.appachhi.plugin.instrumentation;

import com.appachhi.plugin.instrumentation.annotation.AnnotatedMethodInstrumentationFactory;
import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationFactory;

import java.util.List;

public interface InstrumentationConfig {

    List<AnnotatedMethodInstrumentationFactory> getAnnotatedMethodInstrumentationFactories(String classDesc);


    NetworkObjectInstrumentationFactory getNetworkObjectInstrumentationFactory(String className, String methodName, String methodDesc);

    ClassLoader getClassLoader();
}
