package com.github.rzabini.org.approvaltests.spock;

import com.spun.util.persistence.Loader;
import org.approvaltests.Approvals;
import org.approvaltests.namer.ApprovalNamer;
import org.approvaltests.namer.AttributeStackSelector;

public class SpockApprovals extends Approvals {

    static {
        AttributeStackSelector.classNames = new String[]{"org.spockframework.runtime.model.FeatureMetadata"};

        SpockApprovals.namerCreater = new Loader<ApprovalNamer>() {
            @Override
            public ApprovalNamer load() {
                return new ApprovalNamerWithCustomPath(new SpockStackTraceNamer());
            }
        };
    }
}
