package com.appachhi.plugin.instrumentation;


import com.appachhi.plugin.instrumentation.annotation.AnnotatedMethodInstrumentationConfig;
import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationConfig;

import java.util.ArrayList;
import java.util.List;

class InstrumentationConfigBuilder {
    private final ArrayList<NetworkObjectInstrumentationConfig> networkObjectInstrumentationConfigs = new ArrayList<>();
    private final ArrayList<AnnotatedMethodInstrumentationConfig> annotatedMethodInstrumentationConfigs = new ArrayList<>();
    private ClassLoader classLoader;

    InstrumentationConfigBuilder() {
    }

    InstrumentationConfigBuilder addAnnotationMethodInstrumentationConfig(final AnnotatedMethodInstrumentationConfig config) {
        this.annotatedMethodInstrumentationConfigs.add(config);
        return this;
    }

    InstrumentationConfigBuilder addAnnotationMethodInstrumentationConfigs(final AnnotatedMethodInstrumentationConfig[] configs) {
        for (AnnotatedMethodInstrumentationConfig config : configs) {
            addAnnotationMethodInstrumentationConfig(config);
        }
        return this;
    }

    InstrumentationConfigBuilder addNetworkObjectInstrumentationConfig(final NetworkObjectInstrumentationConfig config) {
        this.networkObjectInstrumentationConfigs.add(config);
        return this;
    }

    InstrumentationConfigBuilder addNetworkObjectInstrumentationConfigs(final NetworkObjectInstrumentationConfig[] configs) {
        for (NetworkObjectInstrumentationConfig config : configs) {
            addNetworkObjectInstrumentationConfig(config);
        }
        return this;
    }

    InstrumentationConfigBuilder setClassLoader(final ClassLoader cl) {
        this.classLoader = cl;
        return this;
    }

    InstrumentationConfig build() {
        return new InstrumentationConfigImpl(this.annotatedMethodInstrumentationConfigs, this.networkObjectInstrumentationConfigs, this.classLoader);
    }
}
