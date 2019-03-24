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

public class SpockStackTraceNamer implements ApprovalNamer, Function<StackTraceElement, String> {
    private StackTraceReflectionResult info = getCurrentFileForMethod(new AttributeStackSelector(),this);


    private static StackTraceReflectionResult getCurrentFileForMethod(StackElementSelector stackElementSelector, Function<StackTraceElement, String> testMethodNamer) {
        StackTraceElement trace[] = ThreadUtils.getStackTrace();
        stackElementSelector.increment();
        return getCurrentFileForMethod(stackElementSelector, trace, testMethodNamer);
    }

    private static StackTraceReflectionResult getCurrentFileForMethod(StackElementSelector stackElementSelector,
                                                                      StackTraceElement[] trace, Function<StackTraceElement, String> testMethodNamer) {
        try {
            StackTraceElement element = stackElementSelector.selectElement(trace);
            return getInfo(element, testMethodNamer);
        } catch (Throwable t) {
            throw ObjectUtils.throwAsError(t);
        }
    }

    private static StackTraceReflectionResult getInfo(StackTraceElement element, Function<StackTraceElement, String> testMethodNamer) throws ClassNotFoundException {
        String fullClassName = element.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String fileName = element.getFileName();
        File dir = ClassUtils.getSourceDirectory(ObjectUtils.loadClass(fullClassName), fileName);
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
    public String apply(StackTraceElement stackTraceElement) {
        return readMethodName(stackTraceElement);
    }

    private String readMethodName(StackTraceElement element) {
        String methodName = element.getMethodName();
        String fullClassName = element.getClassName();
        Class clazz = getClazz(fullClassName);

        boolean isSpockFeature = Specification.class.isAssignableFrom(clazz);
        if (isSpockFeature) {
            List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());

            FeatureMetadata featureMetadata = methods.get(0).getAnnotation(FeatureMetadata.class);
            String name = featureMetadata.name();
            if (featureMetadata.parameterNames().length > 0)
                name = name + Arrays.toString(featureMetadata.parameterNames());
            return name;
        } else {
            return methodName;
        }
    }

    private Class<?> getClazz(String fullClassName) {
        try {
            return ObjectUtils.loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}

