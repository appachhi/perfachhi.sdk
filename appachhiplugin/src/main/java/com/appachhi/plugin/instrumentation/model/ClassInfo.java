package com.appachhi.plugin.instrumentation.model;

import java.util.ArrayList;

import org.objectweb.asm.Type;

public class ClassInfo {
    private Type type;
    private ArrayList<AnnotationInfo> annotations = new ArrayList<>();
    private String superclass;
    private String[] interfaces;

    public ClassInfo() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<AnnotationInfo> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(ArrayList<AnnotationInfo> annotations) {
        this.annotations = annotations;
    }

    public String getSuperclass() {
        return superclass;
    }

    public void setSuperclass(String superclass) {
        this.superclass = superclass;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String[] interfaces) {
        this.interfaces = interfaces;
    }
}
