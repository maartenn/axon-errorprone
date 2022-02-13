package com.github.axon_errorprone;

import com.google.common.base.Predicates;
import com.google.errorprone.CompilationTestHelper;
import org.junit.jupiter.api.Test;


class TestInitialResultNotCalledOnSubscriptionQueryResult {
    private final CompilationTestHelper compilationHelper =
        CompilationTestHelper.newInstance(InitialResultNotCalledOnSubscriptionQuery.class, getClass());

    @Test
    void positiveCases() {
        compilationHelper
            .expectErrorMessage("X", Predicates.containsPattern("InitialResultNotCalledOnSubscriptionQuery"))
            .addSourceFile("InitialResultNotCalledOnSQPositiveCases.java")
            .doTest();
    }

    @Test
    void negativeCases() {
        compilationHelper.addSourceFile("InitialResultNotCalledOnSQNegativeCases.java").doTest();
    }
}


