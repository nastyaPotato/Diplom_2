import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.clients.UserClient;
import ru.yandex.objects.User;
import ru.yandex.testdata.UserGenerator;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@DisplayName("Тесты на апдейт пользователя")
public class UserUpdateTest {
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

    @DisplayName("Проверка апдейта имени")
    @Test
    public void userNameCanBeUpdated() {
        String newName = "test123";
        user.setName(newName);
        response = userClient.updateUser(user, token);

        //получаем код респонса и боди
        boolean actualIsUserUpdated = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();
        String updatedName = response.extract().path("user.name");

        int expectedStatusCode = 200;
        boolean expectedBoolean = true;

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actualIsUserUpdated);
        assertEquals(newName, updatedName);
    }

    @DisplayName("Проверка апдейта емейла")
    @Test
    public void userEmailCanBeUpdated() {
        String newEmail = "test-data" + new Random().nextInt(10000) + "@yandex.ru";
        user.setEmail(newEmail);
        response = userClient.updateUser(user, token);

        //получаем код респонса и боди
        boolean actualIsUserUpdated = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();
        String updatedEmail = response.extract().path("user.email");

        int expectedStatusCode = 200;
        boolean expectedBoolean = true;

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actualIsUserUpdated);
        assertEquals(newEmail, updatedEmail);
    }

    @DisplayName("Проверка апдейта пароля")
    @Test
    public void userPasswordCanBeUpdated() {
        String newPassword = "test-data";
        user.setPassword(newPassword);
        response = userClient.updateUser(user, token);
        //нужно попробовать залогинится с новым паролем, так как в респонс апдейта пароль не возвращается(нельзя проверить, обновился ли пароль)
        ValidatableResponse loginResponse = userClient.login(user, token);

        //получаем код респонса и боди для апдейта
        boolean actualIsUserUpdated = response.extract().path("success");
        int actualUpdateStatusCode = response.extract().statusCode();
        //получаем код респонса и боди для логина
        boolean actualIsUserLoggedIn = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();

        //апдейт
        int expectedUpdateStatusCode = 200;
        boolean expectedUpdateBoolean = true;
        //логин
        int expectedLoginStatusCode = 200;
        boolean expectedLoginBoolean = true;

        //проверяем соотвествие для апдейта
        assertEquals(expectedUpdateStatusCode, actualUpdateStatusCode);
        assertEquals(expectedUpdateBoolean, actualIsUserUpdated);

        //проверяем соотвествие для логина
        assertEquals(expectedLoginStatusCode, actualStatusCode);
        assertEquals(expectedLoginBoolean, actualIsUserLoggedIn);
    }

    @DisplayName("Проверка апдейта имени без авторизации")
    @Test
    public void userNameCanNotBeUpdatedWithoutAuth() {
        String newName = "test123";
        user.setName(newName);
        response = userClient.updateUserWithoutAuth(user);

        //получаем код респонса и боди
        boolean actualIsUserUpdated = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();
        String actualResponseErrorMessage = response.extract().path("message");


        int expectedStatusCode = 401;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "You should be authorised";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actualIsUserUpdated);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);

    }

    @DisplayName("Проверка апдейта емейла без авторизации")
    @Test
    public void userEmailCanNotBeUpdatedWithoutAuth() {
        String newEmail = "test-data" + new Random().nextInt(10000) + "@yandex.ru";
        user.setEmail(newEmail);
        response = userClient.updateUserWithoutAuth(user);

        //получаем код респонса и боди
        boolean actualIsUserUpdated = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();
        String actualResponseErrorMessage = response.extract().path("message");


        int expectedStatusCode = 401;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "You should be authorised";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actualIsUserUpdated);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);

    }

    @DisplayName("Проверка апдейта пароля без авторизации")
    @Test
    public void userPasswordCanNotBeUpdatedWithoutAuth() {
        String newPassword = "test-data";
        user.setPassword(newPassword);
        response = userClient.updateUserWithoutAuth(user);

        //получаем код респонса и боди
        boolean actualIsUserUpdated = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();
        String actualResponseErrorMessage = response.extract().path("message");


        int expectedStatusCode = 401;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "You should be authorised";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actualIsUserUpdated);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);

    }

}
