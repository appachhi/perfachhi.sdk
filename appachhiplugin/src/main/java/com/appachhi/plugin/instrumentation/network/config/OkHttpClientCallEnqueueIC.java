package com.appachhi.plugin.instrumentation.network.config;

import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationConfig;
import com.appachhi.plugin.instrumentation.network.hooks.OkHttpClientCallEnqueueInstrumentation;

public class OkHttpClientCallEnqueueIC extends NetworkObjectInstrumentationConfig {
    public OkHttpClientCallEnqueueIC() {
        super(new OkHttpClientCallEnqueueInstrumentation.Factory(), "okhttp3/Call", "enqueue", "(Lokhttp3/Callback;)V");
    }
}
