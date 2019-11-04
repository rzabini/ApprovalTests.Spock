package com.github.rzabini.org.approvaltests.spock

import org.approvaltests.namer.ApprovalNamer
import spock.lang.Specification
import spock.lang.Unroll

class ApprovalNamerSpecification extends Specification {

    def testPathRenamer() {
        given:
        def spockNamer = Mock(ApprovalNamer)
        spockNamer.getSourceFilePath() >> path

        ApprovalNamerWithCustomPath customNamer = new ApprovalNamerWithCustomPath(spockNamer);

        expect:
        customNamer.getSourceFilePath().equals(targetPath)

        where:
        path                                                                             | targetPath
        "C:\\Users\\username\\src\\gwp-api-tests\\.\\src\\test\\groovy\\com\\jhi\\gwp\\" | "C:\\Users\\username\\src\\gwp-api-tests\\.\\src\\test\\resources\\com\\jhi\\gwp\\"
        "/Users/username/src/gwp-api-tests/./src/test/groovy/com/jhi/gwp/"               | "/Users/username/src/gwp-api-tests/./src/test/resources/com/jhi/gwp/"
    }
}
