package io.hybridtheory.mutalisk.common.util;


import java.lang.reflect.Method;

public class TestHelper {
    // should x.class is at least super class of y.class
    // x.getClass().isAssignableFrom(y.getClass());
    public static void unfoldObjectGetMethod(Object x, Object y, UnfoldObjMethod method) {
        if (!x.getClass().isAssignableFrom(y.getClass())) {
            return;
        }

        Method[] methods = x.getClass().getMethods();

        for (Method mex : methods) {
            if (mex.getName().startsWith("get")) {
                method.apply(x, y, mex);
            }
        }
    }

    public static interface UnfoldObjMethod {
        void apply(Object x, Object y, Method method);
    }
}
