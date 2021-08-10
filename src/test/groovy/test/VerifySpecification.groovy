package test

import org.approvaltests.Approvals
import spock.lang.Specification

class VerifySpecification extends Specification {

    def "works on path with repeated names"(){
        expect:
            Approvals.verify('check me')
    }

}
