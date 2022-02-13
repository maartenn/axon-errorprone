import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;

public class InitialResultNotCalledOnSQPositiveCases {
    public InitialResultNotCalledOnSQPositiveCases(QueryGateway queryGateway){
        SubscriptionQueryResult<Void, Object> subscriptionQueryResult = queryGateway.subscriptionQuery(
            new Object(), Void.class, Object.class);
            // BUG: Diagnostic matches: X
            subscriptionQueryResult.updates();
    }
}
