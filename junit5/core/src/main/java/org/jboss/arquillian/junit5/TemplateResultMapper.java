package org.jboss.arquillian.junit5;

import java.util.Map;

import org.jboss.arquillian.test.spi.TestResult;

class TemplateResultMapper {

    private final TestResult aggregatedResult;
    private int invocationCounter;

    TemplateResultMapper(TestResult aggregatedResult) {
        this.aggregatedResult = aggregatedResult;
        this.invocationCounter = 0;
    }

    TestResult nextResult() {
        invocationCounter++;
        if (aggregatedResult == null || aggregatedResult.getStatus() == TestResult.Status.PASSED) {
            return aggregatedResult;
        }
        if (aggregatedResult.getStatus() == TestResult.Status.FAILED
            && aggregatedResult.getThrowable() instanceof IdentifiedTestException) {
            IdentifiedTestException identified = (IdentifiedTestException) aggregatedResult.getThrowable();
            for (Map.Entry<String, Throwable> entry : identified.getCollectedExceptions().entrySet()) {
                if (matchesInvocationIndex(entry.getKey(), invocationCounter)) {
                    return TestResult.failed(entry.getValue());
                }
            }
            return TestResult.passed();
        }
        return aggregatedResult;
    }

    private boolean matchesInvocationIndex(String uniqueId, int invocationIndex) {
        return uniqueId.endsWith("#" + invocationIndex + "]");
    }
}
