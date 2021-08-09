package com.github.rzabini.org.approvaltests.spock;

import com.spun.util.ObjectUtils;
import com.spun.util.StringUtils;
import com.spun.util.ThreadUtils;
import com.spun.util.io.StackElementSelector;
import com.spun.util.tests.StackTraceReflectionResult;
import org.approvaltests.namer.ApprovalNamer;
import org.approvaltests.namer.AttributeStackSelector;
import org.approvaltests.namer.NamerFactory;
import org.approvaltests.writers.Writer;
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
            final Function<StackTraceElement, String> testMethodNamer) {
        final String fullClassName = element.getClassName();
        final String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        final File dir = SourceDirectory.of(fullClassName).toFile();
        return new StackTraceReflectionResult(dir, className, fullClassName, testMethodNamer.apply(element));
    }

    @Override
    public String getApprovalName() {
        return String.format("%s.%s%s", info.getClassName(), info.getMethodName(),
                NamerFactory.getAdditionalInformation());
    }

    @Override
    public String getSourceFilePath() {
        final String sub = NamerFactory.getSubdirectory();
        final String subdirectory = StringUtils.isEmpty(sub) ? "" : sub + File.separator;
        final String baseDir = getBaseDirectory();
        return baseDir + File.separator + subdirectory;
    }

    private String getBaseDirectory() {
        String baseDir = info.getSourceFile().getAbsolutePath();
        if (!StringUtils.isEmpty(NamerFactory.getApprovalBaseDirectory())) {
            final String packageName = info.getFullClassName().substring(0, info.getFullClassName().lastIndexOf("."));
            final String packagepath = packageName.replace('.', File.separatorChar);
            final String currentBase = baseDir.substring(0, baseDir.lastIndexOf(packagepath));
            final String newBase = currentBase + NamerFactory.getApprovalBaseDirectory() + File.separator + packagepath;
            baseDir = ObjectUtils.throwAsError(() -> new File(newBase).getCanonicalPath().toString());
        }
        return baseDir;
    }

    @Override
    public String apply(final StackTraceElement stackTraceElement) {
        return readMethodName(stackTraceElement);
    }

    private String readMethodName(final StackTraceElement element) {
        final String methodName = element.getMethodName();
        final String fullClassName = element.getClassName();
        final Class<?> clazz = getClazz(fullClassName);

        if (isSpockFeature(clazz)) {
            final List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());

            final FeatureMetadata featureMetadata = methods.get(0).getAnnotation(FeatureMetadata.class);
            String name = featureMetadata.name();
            if (featureMetadata.parameterNames().length > 0) {
                name = name + Arrays.toString(featureMetadata.parameterNames());
            }
            return name;
        } else {
            return methodName;
        }
    }

    private boolean isSpockFeature(final Class<?> clazz) {
        return Specification.class.isAssignableFrom(clazz);
    }

    private Class<?> getClazz(final String fullClassName) {
        try {
            return ObjectUtils.loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public File getApprovedFile(final String extensionWithDot) {
        return getFileWithRole(extensionWithDot, Writer.approved);
    }

    @Override
    public File getReceivedFile(final String extensionWithDot) {
        return getFileWithRole(extensionWithDot, Writer.received);
    }

    private File getFileWithRole(final String extensionWithDot, final String role) {
        return new File(getSourceFilePath() + "/" + getApprovalName() + role + extensionWithDot);
    }
}

