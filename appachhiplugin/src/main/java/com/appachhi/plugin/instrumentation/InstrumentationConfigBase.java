package com.appachhi.plugin.instrumentation;


import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationFactory;

public abstract class InstrumentationConfigBase implements InstrumentationConfig {
    public InstrumentationConfigBase() {
    }

    public NetworkObjectInstrumentationFactory getNetworkObjectInstrumentationFactory(final String className, final String methodName, final String methodDesc) {
        return null;
    }
}

