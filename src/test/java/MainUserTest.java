import data.UserRequestData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserAuthSteps;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
public class MainUserTest extends Hooks {

    UserRequestData userRequestData;
    String accessToken;

    @Before
    public void userDataSetUp() {
     userRequestData = new UserRequestData();
     userRequestData.setEmail(RandomStringUtils.randomAlphabetic(8) + "@some.com");
     userRequestData.setPassword(RandomStringUtils.randomAlphabetic(8));
     userRequestData.setName(RandomStringUtils.randomAlphabetic(8));
    }

    @Test
    @DisplayName("Unique user creation")
    @Description("Checking that new user may be created when fields are filled correctly")
    public void userRegisterTest() {
        Response response = UserAuthSteps.createUser(userRequestData);
        accessToken = response.path("accessToken");
        response.then()
                .statusCode(200)
                .body("accessToken", is(notNullValue()));
    }

    @Test
    @DisplayName("User duplicate creation permission")
    @Description("Checking that duplicate user can not be created")
    public void userDuplicateRegisterTest() {
        UserAuthSteps.createUser(userRequestData);
        Response response = UserAuthSteps.createUser(userRequestData);
        accessToken = response.path("accessToken");
        response.then()
                .statusCode(403)
                .body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Log in, via login and password")
    @Description("Checking that courier may authorized when fields are filled correctly")
    public void userLogInTest() {
        UserAuthSteps.createUser(userRequestData);
        Response response = UserAuthSteps.logInUser(userRequestData);
        accessToken = response.path("accessToken");
        response.then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("User data changing")
    @Description("Checking user data may be changed")
    public void userDataChangeTest() {
        Response response = UserAuthSteps.createUser(userRequestData);
        accessToken = response.path("accessToken");
        userRequestData.setEmail(RandomStringUtils.randomAlphabetic(8) + "@some.com");
        userRequestData.setPassword(RandomStringUtils.randomAlphabetic(8));
        userRequestData.setName(RandomStringUtils.randomAlphabetic(8));
        UserAuthSteps.shiftUser(userRequestData, accessToken)
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Unauthorized user data changing")
    @Description("Checking that unauthorized user data can not be changed")
    public void userFailDataChangeTest() {
        Response response = UserAuthSteps.createUser(userRequestData);
        accessToken = response.path("accessToken");
        userRequestData.setEmail(RandomStringUtils.randomAlphabetic(8) + "@some.com");
        userRequestData.setPassword(RandomStringUtils.randomAlphabetic(8));
        userRequestData.setName(RandomStringUtils.randomAlphabetic(8));
        UserAuthSteps.shiftUser(userRequestData, "")
                .then()
                .statusCode(401)
                .body("message", is("You should be authorised"));
    }

    @After
    public void tearDown(){
        UserAuthSteps.deleteUser(accessToken);
    }
}
