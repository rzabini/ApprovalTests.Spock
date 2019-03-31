package com.github.rzabini.org.approvaltests.spock

import com.spun.util.persistence.Loader
import org.approvaltests.Approvals
import org.approvaltests.namer.ApprovalNamer
import org.approvaltests.namer.AttributeStackSelector

import static groovy.util.GroovyCollections.combinations

/**
 * class SpockApprovals
 */
class SpockApprovals extends Approvals {

    static void verifyAllCombinations(Closure function, Object[]... args) {
        verify(combinations(args).collect { "$it => ${function.call(it)}" }.join('\n'))
    }

    static {
        addTestClassAnnotation('org.spockframework.runtime.model.FeatureMetadata')

        Approvals.namerCreater = new Loader<ApprovalNamer>() {
            @Override
            ApprovalNamer load() {
                return new ApprovalNamerWithCustomPath(new SpockStackTraceNamer())
            }
        }
    }

    private static void addTestClassAnnotation(final String annotation) {
        int length = AttributeStackSelector.classNames.length
        AttributeStackSelector.classNames = Arrays.copyOf(
                AttributeStackSelector.classNames, length + 1)
        AttributeStackSelector.classNames[length] = annotation
    }

}
