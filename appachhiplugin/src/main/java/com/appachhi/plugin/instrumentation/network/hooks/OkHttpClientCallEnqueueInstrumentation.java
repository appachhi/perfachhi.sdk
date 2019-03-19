package com.appachhi.plugin.instrumentation.network.hooks;

import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentation;
import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationFactory;

public class OkHttpClientCallEnqueueInstrumentation extends BaseInstrumentation {
    private OkHttpClientCallEnqueueInstrumentation(final String owner, final String name, final String desc) {
        super(owner, name, desc);
    }

    public static class Factory implements NetworkObjectInstrumentationFactory {
        public Factory() {
        }

        public NetworkObjectInstrumentation newObjectInstrumentation(final String className, final String methodName, final String methodDesc) {
            return new OkHttpClientCallEnqueueInstrumentation("com/appachhi/sdk/instrument/network/internal/okhttp/AppachhiOkHttp3Client", "enqueue", "(Lokhttp3/Call;Lokhttp3/Callback;)V");
        }
    }
}