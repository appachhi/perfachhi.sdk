package com.appachhi.plugin.instrumentation.network.config;

import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationConfig;
import com.appachhi.plugin.instrumentation.network.hooks.OkHttpClientCallExecuteInstrumentation;

public class OkHttpClientCallExecuteIC extends NetworkObjectInstrumentationConfig {
    public OkHttpClientCallExecuteIC() {
        super(new OkHttpClientCallExecuteInstrumentation.Factory(), "okhttp3/Call", "execute", "()Lokhttp3/Response;");
    }
}
