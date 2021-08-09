package com.github.rzabini.org.approvaltests.spock

import org.approvaltests.namer.NamerFactory
import spock.lang.Specification

class WhereSpecification extends Specification {

    def setup() {
        NamerFactory.additionalInformation = specificationContext.currentIteration.dataVariables.toString()
    }

    def cleanup() {
        NamerFactory.additionalInformation = null
    }

    def "works on iteration"(String val, int num) {
        expect:
            SpockApprovals.verify("$val -> $num")
        where:
            val << ['22', '1']
            num << [10, 20]
    }
}
