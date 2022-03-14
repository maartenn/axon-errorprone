import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class InitialResultNotCalledOnSQNegativeCases {
    public InitialResultNotCalledOnSQNegativeCases(QueryGateway queryGateway){
        // test whether .initialResult and .updates can be found
        SubscriptionQueryResult<Void, Object> subscriptionQueryResult = queryGateway.subscriptionQuery(
            new Object(), Void.class, Object.class);
            subscriptionQueryResult.initialResult();
            subscriptionQueryResult.updates();

        // test whether ::updates can be found
        SubscriptionQueryResult<Void, Object> subscriptionQueryResult2 = queryGateway.subscriptionQuery(
            new Object(), Void.class, Object.class);
        subscriptionQueryResult2.initialResult();
        Flux.defer(subscriptionQueryResult2::updates);


        // test whether ::initalResult can be found
        SubscriptionQueryResult<Void, Object> subscriptionQueryResult3 = queryGateway.subscriptionQuery(
            new Object(), Void.class, Object.class);
        Mono.defer(subscriptionQueryResult3::initialResult);
        Flux.defer(subscriptionQueryResult3::updates);
    }
}
