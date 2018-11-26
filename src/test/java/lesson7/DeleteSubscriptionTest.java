package lesson7;

import io.qameta.allure.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lesson7.request.RequestModel;

import static lesson7.SubscriptionHelper.createSubscription;
import static lesson7.SubscriptionHelper.deleteSubscription;

import static io.restassured.RestAssured.given;

@Epic("Market Data")
@Feature("Подписка на инструмент")
@Story("Удаление подписки")
@DisplayName("Тесты удаления подписки")
public class DeleteSubscriptionTest{

    @Link("https://fintech-trading-qa.tinkoff.ru/v1/md/docs/#/Subscriptions/md-contacts-subscription-concel")
    @Test
    @DisplayName("Успешное удаление подписки")
    @Description("Позитивный сценарий удаления подписки")
    public void DeleteSubscriptionsSuccessTest(){
        String subscriptionId = createSubscription("TCS_SPBXM", "TCS", "equity", 10.0);
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
    @DisplayName("Удаление удаленной подписки")
    @Description("Проверка успешного удаления подписки, которая была уже удалена ранее")
    public void DeleteSubscriptionsAlreadyDeletedTest(){
        String subscriptionId = createSubscription("TCS_SPBXM", "TCS", "equity", 10.0);
        deleteSubscription(subscriptionId);
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
    @Issue("SB-123")
    @TmsLink("1234")
    @DisplayName("Удаление несуществующей подписки")
    @Description("Проверка получения ошибки при удалении несуществующей подписки")
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
    @DisplayName("Удаление подписки с указанием некорректного id")
    @Description("Проверка получения ошибки при удалении подписки по некорректному id")
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