package com.github.rzabini.org.approvaltests.spock;

import com.spun.util.persistence.Loader;
import org.approvaltests.Approvals;
import org.approvaltests.namer.ApprovalNamer;
import org.approvaltests.namer.AttributeStackSelector;

import java.util.Arrays;

/**
 *
 */
public class SpockApprovals extends Approvals {

    static {
        addTestClassAnnotation("org.spockframework.runtime.model.FeatureMetadata");

        Approvals.namerCreater = new Loader<ApprovalNamer>() {
            @Override
            public ApprovalNamer load() {
                return new ApprovalNamerWithCustomPath(new SpockStackTraceNamer());
            }
        };
    }

    private static void addTestClassAnnotation(final String annotation) {
        final int n = AttributeStackSelector.classNames.length;
        AttributeStackSelector.classNames = Arrays.copyOf(AttributeStackSelector.classNames, n + 1);
        AttributeStackSelector.classNames[n] = annotation;
    }
}
