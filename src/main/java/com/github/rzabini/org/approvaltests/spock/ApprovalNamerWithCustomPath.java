package com.github.rzabini.org.approvaltests.spock;

import org.approvaltests.namer.ApprovalNamer;

public class ApprovalNamerWithCustomPath implements ApprovalNamer {

    private final ApprovalNamer approvalNamer;

    public ApprovalNamerWithCustomPath(ApprovalNamer approvalNamer) {
        this.approvalNamer = approvalNamer;
    }

    @Override
    public String getApprovalName() {
        return approvalNamer.getApprovalName();
    }

    @Override
    public String getSourceFilePath() {
        return approvalNamer.getSourceFilePath().replace("test/groovy", "test/resources");
    }
}
