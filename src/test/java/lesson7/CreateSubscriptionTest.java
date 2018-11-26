package lesson7;

import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import lesson7.entity.CreateSubscriptionInfo;
import lesson7.entity.SubscriptionInfo;
import lesson7.request.RequestModel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static lesson7.SubscriptionHelper.createSubscription;
import static io.restassured.RestAssured.given;

@Epic("Market Data")
@Feature("Подписка на инструмент")
@Story("Создание подписки")
@DisplayName("Тесты создания подписки")
public class CreateSubscriptionTest {

    @Link("https://fintech-trading-qa.tinkoff.ru/v1/md/docs/#/Subscriptions/md-contacts-subscription-create")
    @ParameterizedTest(name = "Создание подписки с price alert ''{3}''")
    @MethodSource("instrumentProvider")
    @Description("Позитивный сценарий создания подписки")
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

        createAttachment(subscriptionInfo.toString());
    }

    private static Stream<Arguments> instrumentProvider() {
        return Stream.of(
                Arguments.of("TCS_SPBXM", "TCS", "equity", 100.9),
                Arguments.of("AAPL_SPBXM", "AAPL", "equity", 0.0),
                Arguments.of("GAZP_TQBR", "GAZP", "equity", -1.66));
    }

    @Test
    @DisplayName("Создание второй подписки для инструмента")
    @Description("Проверка конфликта создания подписки: вторая подписка для того же инструмента")
    public void createSubscriptionSameInstrumentErrorTest(){
        createSubscription("GAZP_TQBR", "GAZP", "equity", 500.0);
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
    @DisplayName("Создание подписки для несуществующего инструмента")
    @Description("Проверка получения ошибки при создании подписки для несуществующего инструмента")
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

    @Attachment("Request")
    private byte[] createAttachment(String attachment) {
        return attachment.getBytes();
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
