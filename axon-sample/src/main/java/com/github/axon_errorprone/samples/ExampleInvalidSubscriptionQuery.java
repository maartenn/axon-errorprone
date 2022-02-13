package com.github.axon_errorprone.samples;

import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;

public class ExampleInvalidSubscriptionQuery {
    // remove / comment this SuppressWarnings-annotation when you want to see compilation failure
    @SuppressWarnings("InitialResultNotCalledOnSubscriptionQuery")
    public ExampleInvalidSubscriptionQuery(QueryGateway queryGateway) {
        SubscriptionQueryResult<Void, Object> subscriptionQueryResult = queryGateway.subscriptionQuery(
            new Object(), Void.class, Object.class);
        subscriptionQueryResult.updates();
    }

}
