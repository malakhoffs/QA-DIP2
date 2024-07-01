import data.Ingredients;
import data.UserRequestData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserAuthSteps;
import steps.OrderSteps;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTest extends Hooks {

    Ingredients ingredients;
    UserRequestData userRequestData;
    String accessToken;

    @Before
    public void userDataSetUp() {
        userRequestData = new UserRequestData();
        userRequestData.setEmail(RandomStringUtils.randomAlphabetic(8) + "@some.com");
        userRequestData.setPassword(RandomStringUtils.randomAlphabetic(8));
        userRequestData.setName(RandomStringUtils.randomAlphabetic(8));
        ingredients = new Ingredients();
        ingredients.addIngredients();
    }

    @Test
    @DisplayName("Show orders of all users")
    @Description("Checking that all user`s orders may be displayed")
    public void getAllOrdersTest(){
        Response response = OrderSteps.getAllOrders();
        response.then().statusCode(200)
                .body("success", is (true))
                .body("orders", is(notNullValue()));
    }

    @Test
    @DisplayName("Show orders of registered user")
    @Description("Checking that all user`s orders may be displayed")
    public void getUserOrdersTest(){
        Response response = UserAuthSteps.createUser(userRequestData);
        accessToken = response.path("accessToken");
        OrderSteps.getUserOrders(accessToken)
                .then().statusCode(200).body("success", is (true))
                .body("orders", is(notNullValue()))
                .body("total", is(notNullValue()))
                .body("totalToday", is(notNullValue()));
    }

    @Test
    @DisplayName("Show orders of unregistered user")
    @Description("Checking that unregistered user may not see its orders")
    public void getUnregisteredUserOrdersTest(){
        OrderSteps.getUserOrders("")
                .then().statusCode(401)
                .body("success", is (false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Make order authorized")
    @Description("Checking that order may be created when authorized")
    public void createOrderTest(){
        Response response = UserAuthSteps.createUser(userRequestData);
        accessToken = response.path("accessToken");
        OrderSteps.createOrder(ingredients, accessToken)
                .then().statusCode(200)
                .body("success", is (true))
                .body("order", is(notNullValue()));
    }

    @Test
    @DisplayName("Make order unauthorized")
    @Description("Checking that order can be ??? created when unauthorized")
    public void createOrderUnregisteredTest() {
        OrderSteps.createOrder(ingredients, "")
                .then().statusCode(200)
                .body("success", is (true))
                .body("order", is(notNullValue()));
    }

    @Test
    @DisplayName("Make empty order authorized")
    @Description("Checking that empty order can not be created when authorized")
    public void createEmptyOrderTestAuthorized(){
        ingredients.removeIngredients();
        Response response = UserAuthSteps.createUser(userRequestData);
        accessToken = response.path("accessToken");
        OrderSteps.createOrder(ingredients, accessToken)
                .then().statusCode(400)
                .body("success", is (false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Make empty order unauthorized")
    @Description("Checking that empty order can not be created when unauthorized")
    public void createEmptyOrderTestUnauthorized(){
        ingredients.removeIngredients();
        OrderSteps.createOrder(ingredients, "")
                .then().statusCode(400)
                .body("success", is (false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Make order with wrong hash of ingredients")
    @Description("Checking that order with ingredients wrong hash providing can not be created (500 Internal Server Error)")
    public void createWrongHashOrderTest(){
        ingredients.removeIngredients();
        ingredients.addWrongHashIngredients();
        Response response = UserAuthSteps.createUser(userRequestData);
        accessToken = response.path("accessToken");
        OrderSteps.createOrder(ingredients, accessToken)
                .then().statusCode(500);
    }

    @After
    public void tearDown(){
        UserAuthSteps.deleteUser(accessToken);
    }
}
