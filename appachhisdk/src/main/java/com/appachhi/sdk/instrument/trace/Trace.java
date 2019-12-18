package com.appachhi.sdk.instrument.trace;




import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})

public @interface Trace {

    boolean enabled() default true;


    String name();
}
