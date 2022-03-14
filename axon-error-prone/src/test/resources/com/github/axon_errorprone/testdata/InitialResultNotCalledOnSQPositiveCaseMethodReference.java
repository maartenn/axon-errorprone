import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import reactor.core.publisher.Flux;

public class InitialResultNotCalledOnSQPositiveCaseMethodReference {
    public InitialResultNotCalledOnSQPositiveCaseMethodReference(QueryGateway queryGateway){
        SubscriptionQueryResult<Void, Object> subscriptionQueryResult = queryGateway.subscriptionQuery(
            new Object(), Void.class, Object.class);
            // BUG: Diagnostic matches: X
            Flux.defer(subscriptionQueryResult::updates);
    }
}
