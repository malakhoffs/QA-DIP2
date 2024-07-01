package steps;

import data.MainPOJO;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static data.Constants.*;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Getting all orders")
    public static Response getAllOrders() {
        return given()
                .baseUri(BASEURL)
                .header("Content-Type", "application/json")
                .when()
                .get(ALL_ORDERS);
    }
    @Step("Getting orders of registered user")
    public static Response getUserOrders(String accessToken) {
        return given()
                .baseUri(BASEURL)
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS);
    }

    @Step ("Forming new order")
    public static Response createOrder(MainPOJO mainPOJO, String accessToken ) {
        return given()
                .baseUri(BASEURL)
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(mainPOJO)
                .when()
                .post(ORDERS);
    }
}


