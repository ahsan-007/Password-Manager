package com.example.passwordmanager;

import java.util.Arrays;

public class Intrinsics {
    private Intrinsics() {
    }
    public static void checkNotNullExpressionValue(Object value, String expression) {
        if (value == null) {
            throw sanitizeStackTrace(new NullPointerException(expression + " must not be null"));
        }
    }
    public static void checkNotNullParameter(Object value, String paramName) {
        if (value == null) {
            throwParameterIsNullNPE(paramName);
        }
    }
    private static void throwParameterIsNullNPE(String paramName) {
        throw sanitizeStackTrace(new NullPointerException(createParameterIsNullExceptionMessage(paramName)));
    }

    private static String createParameterIsNullExceptionMessage(String paramName) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTraceElements[4];
        String className = caller.getClassName();
        String methodName = caller.getMethodName();
        return "Parameter specified as non-null is null: method " + className + "." + methodName + ", parameter " + paramName;
    }
    private static <T extends Throwable> T sanitizeStackTrace(T throwable) {
        return sanitizeStackTrace(throwable, Intrinsics.class.getName());
    }

    static <T extends Throwable> T sanitizeStackTrace(T throwable, String classNameToDrop) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int size = stackTrace.length;

        int lastIntrinsic = -1;
        for (int i = 0; i < size; i++) {
            if (classNameToDrop.equals(stackTrace[i].getClassName())) {
                lastIntrinsic = i;
            }
        }
        StackTraceElement[] newStackTrace = Arrays.copyOfRange(stackTrace, lastIntrinsic + 1, size);
        throwable.setStackTrace(newStackTrace);
        return throwable;
    }
}
