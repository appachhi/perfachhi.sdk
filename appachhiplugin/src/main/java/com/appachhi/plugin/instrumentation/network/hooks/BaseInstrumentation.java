package com.appachhi.plugin.instrumentation.network.hooks;

import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentation;

import org.objectweb.asm.MethodVisitor;

public class BaseInstrumentation implements NetworkObjectInstrumentation {
    private final String owner;
    private final String name;
    private final String desc;

    BaseInstrumentation(final String owner, final String name, final String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    public void injectBefore(final MethodVisitor mv) {
    }

    public void injectAfter(final MethodVisitor mv) {
    }

    public boolean replaceMethod(final MethodVisitor mv, final int opcode) {
        mv.visitMethodInsn(184, this.owner, this.name, this.desc, false);
        return true;
    }

    public String toString() {
        String className = this.getClass().getSimpleName();
        return "[" + className + " " + this.owner + " " + this.name + " " + this.desc + "]";
    }
}
