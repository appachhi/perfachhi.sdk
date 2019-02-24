package com.appachhi.plugin.instrumentation.network;

import org.objectweb.asm.MethodVisitor;

public interface NetworkObjectInstrumentation {
    void injectBefore(MethodVisitor mv);

    void injectAfter(MethodVisitor mv);

    boolean replaceMethod(MethodVisitor mv, int opcode);
}

