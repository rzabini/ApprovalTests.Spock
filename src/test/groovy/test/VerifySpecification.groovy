package test

import com.github.rzabini.org.approvaltests.spock.SpockApprovals
import spock.lang.Specification

class VerifySpecification extends Specification {

    def "works on path with repeated names"(){
        expect:
            SpockApprovals.verify('check me')
    }

}
