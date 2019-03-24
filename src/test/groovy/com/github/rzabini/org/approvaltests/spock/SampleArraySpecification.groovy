package com.github.rzabini.org.approvaltests.spock

import spock.lang.Specification
import com.github.rzabini.org.approvaltests.spock.SpockApprovals as Approvals

class SampleArraySpecification extends Specification {

    def testList() {
        String[] names = ["Llewellyn", "James", "Dan", "Jason", "Katrina"];
        Arrays.sort(names);
        expect:
            Approvals.verifyAll("", names);
    }

    def testList(foo) throws Exception {
        String[] names = ["Llewellyn", "James", "Dan", "Jason", "Katrina", foo];
        Arrays.sort(names);
        expect:
            Approvals.verifyAll("", names);
        where:
            foo = 'foo'
    }

    def testList(int intFoo) throws Exception {
        String[] names = ["Llewellyn", "James", "Dan", "Jason", "Katrina", intFoo];
        Arrays.sort(names);
        expect:
            Approvals.verifyAll("", names);
        where:
            intFoo = 123
    }


    def "it works also for tests with spaces in names"() {
        String[] names = ["Llewellyn", "James", "Dan", "Jason", "Katrina"];
        Arrays.sort(names);
        expect:
            Approvals.verifyAll("", names);
    }
}
