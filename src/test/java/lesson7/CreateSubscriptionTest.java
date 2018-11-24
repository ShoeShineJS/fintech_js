package lesson7;

import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lesson7.entity.CreateSubscriptionInfo;
import lesson7.entity.SubscriptionInfo;
import lesson7.request.RequestModel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class CreateSubscriptionTest {

    @ParameterizedTest(name = "create subscription with price alert ''{3}''")
    @MethodSource("instrumentProvider")
    public void createSubscriptionSuccessTest(String instrumentId, String secName, String secType, Double priceAlert){
        SubscriptionInfo subscriptionInfo = given()
                .spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .body(new CreateSubscriptionInfo(instrumentId, secName, secType, priceAlert))
                .post("/contacts/{siebel_id}/subscriptions")
                .then()
                .specification(RequestModel.getResponseSpecification())
                .assertThat()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("price_alert_subscription.json"))
                .extract()
                .as(SubscriptionInfo.class);

        Assertions.assertAll(
                () -> Assertions.assertEquals(instrumentId , subscriptionInfo.getInstrumentId()),
                () -> Assertions.assertEquals(secName , subscriptionInfo.getSecName()),
                () -> Assertions.assertEquals(secType , subscriptionInfo.getSecType()),
                () -> Assertions.assertEquals(priceAlert , subscriptionInfo.getPriceAlert())
        );
    }

    private static Stream<Arguments> instrumentProvider() {
        return Stream.of(
                Arguments.of("TCS_SPBXM", "TCS", "equity", 100.9),
                Arguments.of("AAPL_SPBXM", "AAPL", "equity", 0.0),
                Arguments.of("GAZP_TQBR", "GAZP", "equity", -1.66));
    }

    @Test
    public void createSubscriptionSameInstrumentErrorTest(){
        SubscriptionHelper.createSubscription("GAZP_TQBR", "GAZP", "equity", 500.0);
        given().spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .body(new CreateSubscriptionInfo("GAZP_TQBR", "GAZP", "equity", 200.0 ))
                .post("/contacts/{siebel_id}/subscriptions")
                .then()
                .specification(RequestModel.getResponseSpecification())
                .statusCode(409)
                .body("error", Matchers.equalTo("could not create subscription: subscription constraint violated"));

    }

    @Test
    public void createSubscriptionInstrumentNotFoundErrorTest(){
        given().spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .body(new CreateSubscriptionInfo("NOT_EXISTING", "GAZP", "equity", 100.0 ))
                .post("/contacts/{siebel_id}/subscriptions")
                .then()
                .specification(RequestModel.getResponseSpecification())
                .statusCode(400)
                .body("error", Matchers.equalTo("instrument not found"));
    }

@AfterEach
    public void SubscriptionCleanUp(){
        SubscriptionHelper.deleteAllSubscriptions();
}

@BeforeAll
    static void prepare(){
      SubscriptionHelper.deleteAllSubscriptions();
}

}
