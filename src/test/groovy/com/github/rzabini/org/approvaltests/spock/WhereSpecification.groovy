package com.github.rzabini.org.approvaltests.spock

import org.approvaltests.Approvals
import spock.lang.Specification

class WhereSpecification extends Specification {

    def "works on iteration"(String val, int num) {
        expect:
            Approvals.verify("$val -> $num")
        where:
            val << ['22', '1']
            num << [10, 20]
    }
}
