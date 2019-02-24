package com.appachhi.plugin.instrumentation.network;

public abstract class NetworkObjectInstrumentationConfig {
    private final NetworkObjectInstrumentationFactory mFactory;
    private final String mClassName;
    private final String mMethodName;
    private final String mMethodDesc;
    private final String mId;

    public NetworkObjectInstrumentationConfig(final NetworkObjectInstrumentationFactory factory, final String className, final String methodName, final String methodDesc) {
        this.mFactory = factory;
        this.mClassName = className;
        this.mMethodName = methodName;
        this.mMethodDesc = methodDesc;
        this.mId = getId(className, methodName, methodDesc);
    }

    public NetworkObjectInstrumentationFactory getFactory() {
        return this.mFactory;
    }

    public String getClassName() {
        return this.mClassName;
    }

    public String getMethodName() {
        return this.mMethodName;
    }

    public String getMethodDesc() {
        return this.mMethodDesc;
    }

    public boolean equals(final Object obj) {
        NetworkObjectInstrumentationConfig o = (NetworkObjectInstrumentationConfig)obj;
        return o.mId.equals(this.mId);
    }

    public int hashCode() {
        return this.mId.hashCode();
    }

    public static String getId(final String className, final String methodName, final String methodDesc) {
        return (new StringBuilder(String.valueOf(className).length() + String.valueOf(methodName).length() + String.valueOf(methodDesc).length())).append(className).append(methodName).append(methodDesc).toString();
    }
}

