package com.github.rzabini.org.approvaltests.spock;

import com.spun.util.ClassUtils;
import com.spun.util.ObjectUtils;
import com.spun.util.ThreadUtils;
import com.spun.util.io.StackElementSelector;
import com.spun.util.tests.StackTraceReflectionResult;
import org.approvaltests.namer.ApprovalNamer;
import org.approvaltests.namer.AttributeStackSelector;
import org.approvaltests.namer.NamerFactory;
import org.spockframework.runtime.model.FeatureMetadata;
import spock.lang.Specification;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Finds text description of Spock specification methods.
 */
class SpockStackTraceNamer implements ApprovalNamer, Function<StackTraceElement, String> {
    private final StackTraceReflectionResult info = getCurrentFileForMethod(new AttributeStackSelector(), this);


    private static StackTraceReflectionResult getCurrentFileForMethod(
            final StackElementSelector stackElementSelector,
            final Function<StackTraceElement, String> testMethodNamer) {
        final StackTraceElement[] trace = ThreadUtils.getStackTrace();
        stackElementSelector.increment();
        return getCurrentFileForMethod(stackElementSelector, trace, testMethodNamer);
    }

    private static StackTraceReflectionResult getCurrentFileForMethod(
            final StackElementSelector stackElementSelector,
            final StackTraceElement[] trace,
            final Function<StackTraceElement, String> testMethodNamer) {
        try {
            final StackTraceElement element = stackElementSelector.selectElement(trace);
            return getInfo(element, testMethodNamer);
        } catch (Throwable t) {
            throw ObjectUtils.throwAsError(t);
        }
    }

    private static StackTraceReflectionResult getInfo(
            final StackTraceElement element,
            final Function<StackTraceElement, String> testMethodNamer) throws ClassNotFoundException {
        final String fullClassName = element.getClassName();
        final String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        final String fileName = element.getFileName();
        final File dir = ClassUtils.getSourceDirectory(ObjectUtils.loadClass(fullClassName), fileName);
        return new StackTraceReflectionResult(dir, className, testMethodNamer.apply(element));
    }

    @Override
    public String getApprovalName() {
        return String.format("%s.%s%s", info.getClassName(), info.getMethodName(),
                NamerFactory.getAndClearAdditionalInformation());
    }

    @Override
    public String getSourceFilePath() {
        return info.getSourceFile().getAbsolutePath() + File.separator;
    }

    @Override
    public String apply(final StackTraceElement stackTraceElement) {
        return readMethodName(stackTraceElement);
    }

    private String readMethodName(final StackTraceElement element) {
        final String methodName = element.getMethodName();
        final String fullClassName = element.getClassName();
        final Class clazz = getClazz(fullClassName);

        if (isSpockFeature(clazz)) {
            final List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());

            final FeatureMetadata featureMetadata = methods.get(0).getAnnotation(FeatureMetadata.class);
            String name = featureMetadata.name();
            if (featureMetadata.parameterNames().length > 0) {
                name = new StringBuilder(name).append(Arrays.toString(featureMetadata.parameterNames())).toString();
            }
            return name;
        } else {
            return methodName;
        }
    }

    private boolean isSpockFeature(final Class clazz) {
        return Specification.class.isAssignableFrom(clazz);
    }

    private Class<?> getClazz(final String fullClassName) {
        try {
            return ObjectUtils.loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

}

