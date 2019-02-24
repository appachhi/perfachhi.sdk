package com.appachhi.plugin.instrumentation.annotation;

import com.appachhi.plugin.instrumentation.InstrumentationContext;
import com.appachhi.plugin.instrumentation.model.AnnotationInfo;

import org.objectweb.asm.commons.AdviceAdapter;

public interface AnnotatedMethodInstrumentationFactory {
    AnnotatedMethodAdapter newAnnotatedMethodInstrumentation(InstrumentationContext instrContext, AdviceAdapter mv, AnnotationInfo ai, String methodName, String methodDesc);
}

