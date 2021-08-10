package com.github.rzabini.org.approvaltests.spock

import org.approvaltests.Approvals
import spock.lang.Specification

class VerifySpecification extends Specification {

    def "can verify a single string"(){
        expect:
            Approvals.verify('check me')
    }

    def "can verify a json object"(){
        expect:
            Approvals.verifyAsJson(['field': 'value'])
    }

    def "can verify xml"(){
        expect:
            Approvals.verifyXml('<parent><child>text</child></parent>')
    }

    def "can verify All"(){
        expect:
            Approvals.verifyAll('objects', ['one', 'two', 'three'])
    }

}
