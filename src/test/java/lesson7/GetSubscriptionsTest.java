package lesson7;

import io.qameta.allure.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lesson7.request.RequestModel;

import static lesson7.SubscriptionHelper.createSubscription;
import static io.restassured.RestAssured.given;

@Epic("Market Data")
@Feature("Подписка на инструмент")
@Story("Список подписок клиента")
@DisplayName("Тесты получения списка подписок")
public class GetSubscriptionsTest{

    @Link("https://fintech-trading-qa.tinkoff.ru/v1/md/docs/#/Subscriptions/md-contacts-subscriptions-retrive")
    @Test
    @DisplayName("Пустой список подписок")
    @Description("Проверка запроса получения подписок, когда подписок у клиента нет")
    public void getSubscriptionsEmptyTest(){
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
    @DisplayName("Непустой список подписок")
    @Description("Проверка получения непустого списка подписок")
    public void getSubscriptionsNonEmptyTest(){
        String sub1 = createSubscription("TCS_SPBXM", "TCS", "equity", 10.0);
        String sub2 = createSubscription("AAPL_SPBXM", "AAPL", "equity", 300.0);
        given().spec(RequestModel.getRequestSpecification())
                .pathParam("siebel_id", "yu.shilkova")
                .queryParam("request_id", "6f994192-e701-11e8-9f32-f2801f1b9fd1")
                .queryParam("system_code", "T-API")
                .get("/contacts/{siebel_id}/subscriptions")
                .then()
                .assertThat()
                .statusCode(200)
                .body("", Matchers.hasSize(2))
                .body("id", Matchers.containsInAnyOrder(sub1,sub2));
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