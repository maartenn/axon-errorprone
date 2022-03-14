package com.github.axon_errorprone;

import com.google.common.base.Predicates;
import com.google.errorprone.CompilationTestHelper;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;


class TestInitialResultNotCalledOnSubscriptionQueryResult {
    private final CompilationTestHelper compilationHelper =
        CompilationTestHelper.newInstance(InitialResultNotCalledOnSubscriptionQuery.class, getClass());

    @Test
    void positiveCaseMethodInvocation() {
        compilationHelper
            .expectErrorMessage("X", Predicates.containsPattern("InitialResultNotCalledOnSubscriptionQuery"))
            .addSourceFile("InitialResultNotCalledOnSQPositiveCaseMethodInvocation.java")
            .doTest();
    }

    @Test
    void positiveCasesMethodReference() {
        compilationHelper
            .expectErrorMessage("X", Predicates.containsPattern("InitialResultNotCalledOnSubscriptionQuery"))
            .addSourceFile("InitialResultNotCalledOnSQPositiveCaseMethodReference.java")
            .doTest();
    }

    @Test
    void negativeCases() {
        compilationHelper.addSourceFile("InitialResultNotCalledOnSQNegativeCases.java").doTest();
    }
}


