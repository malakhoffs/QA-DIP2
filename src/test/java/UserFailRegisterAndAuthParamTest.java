import data.UserRequestData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.UserAuthSteps;

import static org.hamcrest.CoreMatchers.is;

@RunWith (Parameterized.class)
public class UserFailRegisterAndAuthParamTest extends BaseTest {
    private final String email;
    private final String password;
    private final String name;

    @Parameterized.Parameters(name = "{index} Value = {0}")
    public static Object[][] getData() {
        return new Object[][]{
                {"", "", ""},
                {"A1B2C3D4^7*)Lm@-<C@some.com", "Fi&x#+hyB%xsJ@3", ""},
                {"Ip[<.imU}^^gyb>?s@some.com", "", "A%%jii*JN:!^nu"},
                {"", "Th^h9Mm<@#n*(+==JS9M", "A%%jii*JN:!^nu"}

        };
    }
    public UserFailRegisterAndAuthParamTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Test
    @DisplayName("User registering with missing or incorrect fields")
    @Description("Checking that user registration requires filling of all fields")
    public void failRegistrationTest() {
        UserRequestData userRequestData = new UserRequestData(email, password, name);
        Response response = UserAuthSteps.createUser(userRequestData);
        response.then()
                .assertThat().statusCode(403).body("message", is ("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("User failed authorization with missing fields or unregistered")
    @Description("Checking that unregistered user can not be authorized")
    public void failAuthorizationTest() {
        UserRequestData userRequestData = new UserRequestData(email, password);
        Response response = UserAuthSteps.logInUser(userRequestData);
        response.then()
                .assertThat().statusCode(401).body("message", is("email or password are incorrect"));
    }
}
