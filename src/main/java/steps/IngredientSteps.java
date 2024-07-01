package steps;

import io.qameta.allure.Step;
import static data.Constants.BASEURL;
import static data.Constants.INGREDIENTS;
import static io.restassured.RestAssured.given;
public class IngredientSteps {

    @Step("Getting ingredient hash")
    public static String returnIngredient() {
        return given()
                .baseUri(BASEURL)
                .header("Content-Type", "application/json")
                .when()
                .get(INGREDIENTS)
                .then().extract().body().jsonPath().getString("data[0]._id");
    }
}
