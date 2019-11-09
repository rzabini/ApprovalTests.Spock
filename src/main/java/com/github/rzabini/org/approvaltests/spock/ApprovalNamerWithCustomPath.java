package com.github.rzabini.org.approvaltests.spock;

import org.approvaltests.namer.ApprovalNamer;

import java.io.File;
import java.util.regex.Matcher;

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
        return approvalNamer.getSourceFilePath().replaceAll("test"+ Matcher.quoteReplacement(File.separator) + "(groovy|java)",
                "test" + File.separator + "resources");
    }
}
