package steps;

import data.MainPOJO;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static data.Constants.*;
import static io.restassured.RestAssured.given;

public class UserAuthSteps {

    @Step ("Registering new user")
    public static Response createUser (MainPOJO mainPOJO) {
        return given()
                .baseUri(BASEURL)
                .header("Content-Type", "application/json")
                .body(mainPOJO)
                .when()
                .post(REGISTER);
    }

    @Step ("Logging in user")
    public static Response logInUser (MainPOJO mainPOJO) {
        return given()
                .baseUri(BASEURL)
                .header("Content-Type", "application/json")
                .body(mainPOJO)
                .when()
                .post(LOGIN);
    }

    @Step ("Changing user data")
    public static Response shiftUser(MainPOJO mainPOJO, String accessToken ) {
        return given()
                .baseUri(BASEURL)
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(mainPOJO)
                .when()
                .patch(USER);
    }

    @Step ("Deleting user")
    public static void deleteUser(String accessToken) {
        if (accessToken != null) {
            given()
                    .baseUri(BASEURL)
                    .header("Content-Type", "application/json")
                    .header("Authorization", accessToken)
                    .delete(USER);
        } else {
            System.out.println("Something went wrong, nothing to remove");
        }
    }
}
