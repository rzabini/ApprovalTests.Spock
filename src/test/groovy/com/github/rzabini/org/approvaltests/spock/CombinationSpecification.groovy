package com.github.rzabini.org.approvaltests.spock

import spock.lang.Specification

class CombinationSpecification extends Specification {

    def testCombinations() {
        Integer[] points = [4, 5, 10]
        String[] words = ["Bookkeeper", "applesauce"]
        expect:
            SpockApprovals.verifyAllCombinations({ i, s -> s.substring(0, i)} , points , words );
    }
}
