package com.appachhi.plugin.instrumentation;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;

public class InstrumentedAttribute extends Attribute {
    private final String mExtra;

    private InstrumentedAttribute(final String extra) {
        super(InstrumentedAttribute.class.getSimpleName());
        this.mExtra = extra;
    }

    public boolean isUnknown() {
        return false;
    }

    public boolean isCodeAttribute() {
        return false;
    }

    protected Attribute read(final ClassReader cr, final int off, final int len, final char[] buf, final int codeOff, final Label[] labels) {
        return new InstrumentedAttribute(cr.readUTF8(off, buf));
    }

    protected ByteVector write(final ClassWriter cw, final byte[] code, final int len, final int maxStack, final int maxLocals) {
        return (new ByteVector()).putShort(cw.newUTF8(this.mExtra));
    }
}


