package lesson7;

import lesson7.entity.CreateSubscriptionInfo;
import lesson7.entity.SubscriptionInfo;
import lesson7.request.RequestModel;

import static io.restassured.RestAssured.given;

public class SubscriptionHelper {

    public void deleteAllSubscriptions(){
        SubscriptionInfo subscriptionInfo[] = given().spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .get("/contacts/{siebel_id}/subscriptions")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(SubscriptionInfo[].class);

        for(SubscriptionInfo subscr : subscriptionInfo) {
            deleteSubscription(subscr.getSubscriptionId());
        }
    }

    public void deleteSubscription(String subscriptionId){
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

    public String createSubscription(String instrumentId, String secName, String secType, Double priceAlert){
        String id = given().spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .body(new CreateSubscriptionInfo(instrumentId, secName, secType, priceAlert))
                .post("/contacts/{siebel_id}/subscriptions")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("id");
        return id;
    }
}