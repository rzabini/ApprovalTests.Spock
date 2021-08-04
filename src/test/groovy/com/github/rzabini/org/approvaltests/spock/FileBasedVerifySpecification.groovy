package com.github.rzabini.org.approvaltests.spock

import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path

class FileBasedVerifySpecification extends Specification {
    @TempDir
    public Path testFolder //= new TemporaryFolder()

    private Path baseFolder

    def setup() {
        baseFolder = Files.createDirectories(testFolder.resolve('testoutput'))
        Path file = Files.createFile(baseFolder.resolve('sample.txt'))
        file.toFile().text = 'sample output'
    }

    def "can verify each file in directory"() {
        expect:
            SpockApprovals.verifyEachFileInDirectory(baseFolder.toFile())
    }

    def "can verify each file in directory with filter"(){
        expect:
            SpockApprovals.verifyEachFileInDirectory(baseFolder.toFile(),
                    { File afile -> afile.name == 'sample.txt' } as FileFilter)
    }

}
