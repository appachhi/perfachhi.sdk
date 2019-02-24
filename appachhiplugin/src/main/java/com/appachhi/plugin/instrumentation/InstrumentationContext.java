package com.appachhi.plugin.instrumentation;

import java.util.HashMap;

public class InstrumentationContext {
    public final HashMap<String, Object> clazz = new HashMap<>();
    public final HashMap<String, Object> method = new HashMap<>();

    public InstrumentationContext() {
    }
}
