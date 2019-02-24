package com.appachhi.plugin.instrumentation.model;

import java.util.HashMap;
import java.util.Objects;

import org.objectweb.asm.Type;

public class AnnotationInfo {
    private Type type;
    public HashMap<String, Object> values = new HashMap<>();

    public AnnotationInfo() {
    }

    public void setType(Type type) {
        this.type = type;
    }

    public HashMap<String, Object> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationInfo that = (AnnotationInfo) o;
        return type.equals(that.type) &&
                Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, values);
    }
}
