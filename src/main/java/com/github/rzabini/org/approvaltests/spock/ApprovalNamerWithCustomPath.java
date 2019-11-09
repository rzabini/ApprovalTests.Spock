package com.github.rzabini.org.approvaltests.spock;

import org.approvaltests.namer.ApprovalNamer;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Changes path of accepted files.
 */
public class ApprovalNamerWithCustomPath implements ApprovalNamer {

    private static final String TEST_DIRECTORY_NAME = "test";
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
        return approvalNamer.getSourceFilePath().replaceAll(
                TEST_DIRECTORY_NAME + Matcher.quoteReplacement(File.separator) + "(groovy|java)",
                TEST_DIRECTORY_NAME + File.separator + "resources");
    }
}
