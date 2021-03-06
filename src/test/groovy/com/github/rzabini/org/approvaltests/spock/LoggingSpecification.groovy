package com.github.rzabini.org.approvaltests.spock

import dk.bitcraft.lc.JUnit4LogCollector
import org.junit.Rule
import spock.lang.Specification

import java.util.logging.Logger

class LoggingSpecification extends Specification {

    Logger testLogger = Logger.getLogger('LoggingApplication');


    @Rule
    public JUnit4LogCollector collector = new JUnit4LogCollector(testLogger);


    def "can verify logging"(){
        when:
            new LoggingApplication("Sample").doSomethingAndLog("hello world")
        then:
            SpockApprovals.verifyAll('', collector.rawLogs.message)
    }

}

class LoggingApplication {

    private static Logger logger = Logger.getLogger('LoggingApplication');

    private final String name

    public LoggingApplication(String name){
        this.name = name
    }

    void doSomethingAndLog(String message){
        logger.info(String.format("Application %s says: %s", name, message));
        logger.fine(String.format("Application %s says fine", name));
        logger.warning(String.format("Application %s says warning", name));
    }
}