package com.github.rzabini.org.approvaltests.spock

import org.approvaltests.combinations.CombinationApprovals
import spock.lang.Specification

class CombinationSpecification extends Specification {

    def testCombinations() {
        Integer[] points = [4, 5, 10]
        String[] words = ["Bookkeeper", "applesauce"]
        expect:
            CombinationApprovals.verifyAllCombinations({ i, s -> s.substring(0, i) }, points, words);
    }
}
