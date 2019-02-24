package com.appachhi.plugin.instrumentation;


import com.appachhi.plugin.instrumentation.annotation.AnnotatedMethodInstrumentationConfig;
import com.appachhi.plugin.instrumentation.annotation.AnnotatedMethodInstrumentationFactory;
import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationConfig;
import com.appachhi.plugin.instrumentation.network.NetworkObjectInstrumentationFactory;

import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

public class InstrumentationConfigImpl implements InstrumentationConfig {
    private final List<AnnotatedMethodInstrumentationConfig> annotationInstrumentationConfigs;
    private final List<NetworkObjectInstrumentationConfig> networkObjectInstrumentationConfigs;
    private final ClassLoader classLoader;
    Logger L = LoggerFactory.getLogger("AppacchiSDKPlugin");
    private final HashSet<String> missingClasses = new HashSet<>();

    InstrumentationConfigImpl(final List<AnnotatedMethodInstrumentationConfig> annotationInstrumentationConfigs, final List<NetworkObjectInstrumentationConfig> objectInstrumentationConfigs, final ClassLoader classLoader) {
        this.annotationInstrumentationConfigs = Collections.unmodifiableList(annotationInstrumentationConfigs);
        this.networkObjectInstrumentationConfigs = Collections.unmodifiableList(objectInstrumentationConfigs);
        if (classLoader != null) {
            this.classLoader = classLoader;
        } else {
            this.classLoader = this.getClass().getClassLoader();
        }

    }

    @Nullable
    public NetworkObjectInstrumentationFactory getNetworkObjectInstrumentationFactory(final String className, final String methodName, final String methodDesc) {
        Type classType = Type.getObjectType(className);
        if (classType.getSort() != 10) {
            return null;
        } else {
            Class qcl = null;

            try {
                qcl = Class.forName(classType.getClassName(), false, this.classLoader);
            } catch (Exception | LinkageError var15) {
                return null;
            }

            for (NetworkObjectInstrumentationConfig networkObjConfig : this.networkObjectInstrumentationConfigs) {
                Type t = Type.getObjectType(networkObjConfig.getClassName());
                String cn = t.getClassName();
                if (!this.missingClasses.contains(cn)) {
                    try {
                        Class<?> cl = Class.forName(cn, false, this.classLoader);
                        boolean isInst = cl.isAssignableFrom(qcl);
                        boolean methodMatch = networkObjConfig.getMethodName().equals(methodName);
                        boolean descMatch = networkObjConfig.getMethodDesc().equals(methodDesc);
                        if (isInst && methodMatch && descMatch) {
                            return networkObjConfig.getFactory();
                        }
                    } catch (Exception | LinkageError var14) {
                        this.missingClasses.add(cn);
                    }
                }
            }

            return null;
        }
    }

    @Nullable
    public List<AnnotatedMethodInstrumentationFactory> getAnnotatedMethodInstrumentationFactories(final String classDesc) {
        ArrayList<AnnotatedMethodInstrumentationFactory> list = null;

        for (AnnotatedMethodInstrumentationConfig config : this.annotationInstrumentationConfigs) {
            if (config.getClassDesc().equals(classDesc)) {
                if (list == null) {
                    list = new ArrayList<>();
                }

                list.add(config.getFactory());
            }
        }

        return list;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
}

