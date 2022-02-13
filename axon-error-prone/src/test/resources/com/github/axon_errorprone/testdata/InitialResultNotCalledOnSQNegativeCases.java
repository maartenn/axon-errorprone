import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;

public class InitialResultNotCalledOnSQNegativeCases {
    public InitialResultNotCalledOnSQNegativeCases(QueryGateway queryGateway){
        SubscriptionQueryResult<Void, Object> subscriptionQueryResult = queryGateway.subscriptionQuery(
            new Object(), Void.class, Object.class);
            subscriptionQueryResult.initialResult();
            subscriptionQueryResult.updates();
    }
}
