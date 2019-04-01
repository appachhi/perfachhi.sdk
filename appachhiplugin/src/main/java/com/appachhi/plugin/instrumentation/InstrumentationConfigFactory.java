package com.appachhi.plugin.instrumentation;


import com.appachhi.plugin.instrumentation.annotation.AnnotatedMethodInstrumentationConfig;
import com.appachhi.plugin.instrumentation.annotation.AppachhiMethodTraceConfig;
import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationConfig;
import com.appachhi.plugin.instrumentation.network.config.OkHttpClientCallEnqueueIC;
import com.appachhi.plugin.instrumentation.network.config.OkHttpClientCallExecuteIC;

class InstrumentationConfigFactory {
    private final AnnotatedMethodInstrumentationConfig[] annotationConfigs = new AnnotatedMethodInstrumentationConfig[]{
            new AppachhiMethodTraceConfig()
    };
    private final ClassLoader classLoader;
    private final NetworkObjectInstrumentationConfig[] networkConfigs = new NetworkObjectInstrumentationConfig[]{
            new OkHttpClientCallEnqueueIC(), new OkHttpClientCallExecuteIC()
    };

    InstrumentationConfigFactory(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    InstrumentationConfig newConfig() {
        return new InstrumentationConfigBuilder()
                .addAnnotationMethodInstrumentationConfigs(annotationConfigs)
                .addNetworkObjectInstrumentationConfigs(networkConfigs)
                .setClassLoader(classLoader)
                .build();
    }
}

