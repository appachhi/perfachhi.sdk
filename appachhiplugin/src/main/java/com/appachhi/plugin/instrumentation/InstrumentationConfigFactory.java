package com.appachhi.plugin.instrumentation;


import com.appachhi.plugin.instrumentation.annotation.AnnotatedMethodInstrumentationConfig;
import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationConfig;

class InstrumentationConfigFactory {
    private final AnnotatedMethodInstrumentationConfig[] annotationConfigs = new AnnotatedMethodInstrumentationConfig[]{};
    private final ClassLoader classLoader;
    private final NetworkObjectInstrumentationConfig[] networkConfigs = new NetworkObjectInstrumentationConfig[]{};

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

