package com.appachhi.plugin.instrumentation.annotation;

public abstract class AnnotatedMethodInstrumentationConfig {
    private final AnnotatedMethodInstrumentationFactory mFactory;
    private final String mClassDesc;

    public AnnotatedMethodInstrumentationConfig(final AnnotatedMethodInstrumentationFactory factory, final String classDesc) {
        if (classDesc == null) {
            throw new IllegalArgumentException("className was null");
        } else {
            this.mFactory = factory;
            this.mClassDesc = classDesc;
        }
    }

    public AnnotatedMethodInstrumentationFactory getFactory() {
        return this.mFactory;
    }

    public String getClassDesc() {
        return this.mClassDesc;
    }

    @Override
    public boolean equals(Object obj) {
        AnnotatedMethodInstrumentationConfig o = (AnnotatedMethodInstrumentationConfig) obj;
        return o.mClassDesc.equals(this.mClassDesc);
    }

    public int hashCode() {
        return this.mClassDesc.hashCode();
    }
}

