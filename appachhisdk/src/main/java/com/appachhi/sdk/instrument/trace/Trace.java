package com.appachhi.sdk.instrument.trace;

import android.support.annotation.Keep;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Keep
public @interface Trace {
    @Keep
    boolean enabled() default true;

    @Keep
    String name();
}
