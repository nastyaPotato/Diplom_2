import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.clients.UserClient;
import ru.yandex.objects.User;
import ru.yandex.testdata.UserGenerator;

import static org.junit.Assert.assertEquals;

@DisplayName("Тесты на логин")
public class UserLoginTest {
    private UserClient userClient;
    private User user;
    private ValidatableResponse response;
    private String token;

    @Before
    public void setUp() throws InterruptedException {
        userClient = new UserClient();
        user = UserGenerator.getDefaultUser();
        response = userClient.createUser(user);
        token = response.extract().path("accessToken");
        Thread.sleep(2000);
    }

    @After
    public void cleanUp() {
        if (token != null && !token.isEmpty()) {
            userClient.deleteUser(token);
        }
    }

    @DisplayName("Успешный логин")
    @Test
    public void userCanLogin() {
        //получаем респонс создания юзера
        response = userClient.login(user, token);

        //получаем код респонса и боди
        boolean actualIsUserLoggedIn = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();

        int expectedStatusCode = 200;
        boolean expectedBoolean = true;

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actualIsUserLoggedIn);
    }

    @DisplayName("Логин с неправильным паролем")
    @Test
    public void userCanNotLoginWithInvalidPassword() {
        //получаем респонс создания юзера
        user.setPassword("12344");
        response = userClient.login(user, token);

        //получаем код респонса и боди
        boolean actualIsUserLoggedIn = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();
        String actualResponseErrorMessage = response.extract().path("message");

        int expectedStatusCode = 401;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "email or password are incorrect";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actualIsUserLoggedIn);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
    }

    @DisplayName("Логин с неправильным логином")
    @Test
    public void userCanNotLoginWithInvalidLogin() {
        //получаем респонс создания юзера
        user.setEmail("test-data@test.com");
        response = userClient.login(user, token);

        //получаем код респонса и боди
        boolean actualIsUserLoggedIn = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();
        String actualResponseErrorMessage = response.extract().path("message");

        int expectedStatusCode = 401;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "email or password are incorrect";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actualIsUserLoggedIn);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
    }

    @DisplayName("Логин с неправильным логином и паролем")
    @Test
    public void userCanNotLoginWithInvalidCredentials() {
        //получаем респонс создания юзера
        user.setEmail("test-data123@test.com");
        user.setPassword("56");
        response = userClient.login(user, token);

        //получаем код респонса и боди
        boolean actualIsUserLoggedIn = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();
        String actualResponseErrorMessage = response.extract().path("message");

        int expectedStatusCode = 401;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "email or password are incorrect";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actualIsUserLoggedIn);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
    }
}
