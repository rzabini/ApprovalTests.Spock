package com.github.rzabini.org.approvaltests.spock

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class FileBasedVerifySpecification extends Specification {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder()

    private File baseFolder

    def setup() {
        baseFolder = testFolder.newFolder("testOutput")
        File file = new File(baseFolder, 'sample.txt')
        file.text = "sample output"
    }

    def "can verify each file in directory"(){
        expect:
            SpockApprovals.verifyEachFileInDirectory(baseFolder)
    }

    def "can verify each file in directory with filter"(){
        expect:
            SpockApprovals.verifyEachFileInDirectory(baseFolder,
                    {File afile -> afile.name == 'sample.txt'} as FileFilter)
    }

}
