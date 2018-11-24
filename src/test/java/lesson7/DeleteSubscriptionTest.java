package lesson7;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lesson7.request.RequestModel;

import static io.restassured.RestAssured.given;

public class DeleteSubscriptionTest{

    @Test
    public void DeleteSubscriptionsSuccessTest(){
        String subscriptionId = SubscriptionHelper.createSubscription("TCS_SPBXM", "TCS", "equity", 10.0);
        given().spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .pathParam("subscription_id",subscriptionId)
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .delete("/contacts/{siebel_id}/subscriptions/{subscription_id}")
                .then()
                .assertThat()
                .statusCode(200);

        given().spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .get("/contacts/{siebel_id}/subscriptions")
                .then()
                .specification(RequestModel.getResponseSpecification())
                .assertThat()
                .statusCode(200)
                .body("", Matchers.hasSize(0));
    }

    @Test
    public void DeleteSubscriptionsAlreadyDeletedTest(){
        String subscriptionId = SubscriptionHelper.createSubscription("TCS_SPBXM", "TCS", "equity", 10.0);
        SubscriptionHelper.deleteSubscription(subscriptionId);
        given().spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .pathParam("subscription_id",subscriptionId)
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .delete("/contacts/{siebel_id}/subscriptions/{subscription_id}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void DeleteSubscriptionsNonExistedTest(){
        given().spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .pathParam("subscription_id","7a74b774-ea97-11e8-926a-02a0d1954ecb")
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .delete("/contacts/{siebel_id}/subscriptions/{subscription_id}")
                .then()
                .assertThat()
                .statusCode(404)
                .body("error", Matchers.equalTo("could not cancel subscription: subscription not found"));
    }

    @Test
    public void DeleteSubscriptionsInvalidIdTest(){
        given().spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .pathParam("subscription_id","-1")
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .delete("/contacts/{siebel_id}/subscriptions/{subscription_id}")
                .then()
                .assertThat()
                .statusCode(500)
                .body("error", Matchers.equalTo("could not cancel subscription: pq: invalid input syntax for uuid: \"-1\""));
    }

    @BeforeAll
    static void prepare(){
        SubscriptionHelper.deleteAllSubscriptions();
    }


}