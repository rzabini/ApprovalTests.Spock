package com.github.rzabini.org.approvaltests.spock;

import org.approvaltests.namer.ApprovalNamer;

/**
 * Changes path of accepted files.
 */
public class ApprovalNamerWithCustomPath implements ApprovalNamer {

    private final ApprovalNamer approvalNamer;

    ApprovalNamerWithCustomPath(final ApprovalNamer approvalNamer) {
        this.approvalNamer = approvalNamer;
    }

    @Override
    public String getApprovalName() {
        return approvalNamer.getApprovalName();
    }

    @Override
    public String getSourceFilePath() {
        return approvalNamer.getSourceFilePath().replaceAll("test/(groovy|java)", "test/resources");
    }
}
