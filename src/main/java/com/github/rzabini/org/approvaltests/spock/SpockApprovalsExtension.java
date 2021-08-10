package com.github.rzabini.org.approvaltests.spock;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.approvaltests.Approvals;
import org.approvaltests.namer.AttributeStackSelector;
import org.approvaltests.namer.NamerFactory;
import org.spockframework.runtime.extension.IGlobalExtension;
import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.SpecInfo;

import java.util.Arrays;
import java.util.Map;

/**
 * Spock Extension to allow use of <a href="https://github.com/approvals/ApprovalTests.Java">ApprovalTests.Java</a>.
 */
public class SpockApprovalsExtension implements IGlobalExtension {

    private static void addTestClassAnnotation() {
        final int length = AttributeStackSelector.classNames.length;
        AttributeStackSelector.classNames = Arrays.copyOf(
                AttributeStackSelector.classNames, length + 1);
        AttributeStackSelector.classNames[length] = "org.spockframework.runtime.model.FeatureMetadata";
    }

    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    @Override
    public void start() {
        addTestClassAnnotation();
        Approvals.namerCreater = SpockStackTraceNamer::new;
    }

    @Override
    public void visitSpec(final SpecInfo spec) {
        spec.addSetupInterceptor(new ApprovalsAdditionalInformationInterceptor());
    }

    static class ApprovalsAdditionalInformationInterceptor implements IMethodInterceptor {

        @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
        @Override
        public void intercept(final IMethodInvocation invocation) throws Throwable {
            final Map<String, Object> dataVariables = invocation.getIteration().getDataVariables();
            if (dataVariables.isEmpty()) {
                NamerFactory.additionalInformation = null;
            } else {
                NamerFactory.additionalInformation = Arrays.toString(dataVariables.entrySet().toArray());
            }

            invocation.proceed();
        }
    }
}
