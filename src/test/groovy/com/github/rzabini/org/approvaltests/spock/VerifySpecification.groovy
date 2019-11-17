package com.github.rzabini.org.approvaltests.spock

import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class VerifySpecification extends Specification {

    def "can verify a single string"(){
        expect:
            SpockApprovals.verify('check me')
    }

    def "can verify a json object"(){
        expect:
            SpockApprovals.verifyAsJson(['field':'value'])
    }

    def "can verify xml"(){
        expect:
            SpockApprovals.verifyXml('<parent><child>text</child></parent>')
    }

    def "can verify All"(){
        expect:
            SpockApprovals.verifyAll('objects', ['one', 'two', 'three'])
    }

}
