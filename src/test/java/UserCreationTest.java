import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.clients.UserClient;
import ru.yandex.objects.User;
import ru.yandex.testdata.UserGenerator;

import static org.junit.Assert.assertEquals;

@DisplayName("Тесты на создание пользователей")
public class UserCreationTest {

    private UserClient userClient;
    private User user;
    private ValidatableResponse responseCreate;

    @Before
    public void setUp() throws InterruptedException {
        userClient = new UserClient();
        user = UserGenerator.getDefaultUser();
        Thread.sleep(2000);
    }

    @After
    public void cleanUp() {
        String token = responseCreate.extract().path("accessToken");
        if (token != null && !token.isEmpty()) {
            userClient.deleteUser(token);
        }
    }

    @DisplayName("Создание пользователя")
    @Test
    public void userCanBeCreated() {
        //получаем респонс создания юзера
        responseCreate = userClient.createUser(user);

        //получаем код респонса и боди
        boolean actualIsUserCreated = responseCreate.extract().path("success");
        int actualStatusCode = responseCreate.extract().statusCode();

        int expectedStatusCode = 200;
        boolean expectedBoolean = true;

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actualIsUserCreated);
    }

    @DisplayName("Создание пользователя, который уже зарегестрирован")
    @Test
    public void identicalUserCanNotBeCreated() {
        //создали юзера первый раз
        userClient.createUser(user);
        //создали такого же юзера второй раз и записали респонс
        responseCreate = userClient.createUser(user);

        //получаем код респонса и боди
        int actualStatusCode = responseCreate.extract().statusCode();
        String actualResponseErrorMessage = responseCreate.extract().path("message");

        //задаем ожидаемый статус и боди
        int expectedStatusCode = 403;
        String expectedResponseErrorMessage = "User already exists";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
    }

    @DisplayName("Создание пользователя без пароля")
    @Test
    public void userWithoutPasswordCannotBeCreated() {
        //задали курьера без пароля
        user.setPassword("");
        //отправили реквест на создание курьера без пароля и записали респонс
        responseCreate = userClient.createUser(user);

        //получаем код респонса и боди
        int actualStatusCode = responseCreate.extract().statusCode();
        boolean actualIsUserCreated = responseCreate.extract().path("success");
        String actualResponseErrorMessage = responseCreate.extract().path("message");

        //задаем ожидаемый статус и боди
        int expectedStatusCode = 403;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "Email, password and name are required fields";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
        assertEquals(expectedBoolean, actualIsUserCreated);

    }

    @DisplayName("Создание пользователя без емейла")
    @Test
    public void userWithoutEmailCannotBeCreated() {
        //задали курьера без пароля
        user.setEmail("");
        //отправили реквест на создание курьера без пароля и записали респонс
        responseCreate = userClient.createUser(user);

        //получаем код респонса и боди
        int actualStatusCode = responseCreate.extract().statusCode();
        boolean actualIsUserCreated = responseCreate.extract().path("success");
        String actualResponseErrorMessage = responseCreate.extract().path("message");

        //задаем ожидаемый статус и боди
        int expectedStatusCode = 403;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "Email, password and name are required fields";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
        assertEquals(expectedBoolean, actualIsUserCreated);

    }

    @DisplayName("Создание пользователя без имени")
    @Test
    public void userWithoutNameCannotBeCreated() {
        //задали курьера без пароля
        user.setName("");
        //отправили реквест на создание курьера без пароля и записали респонс
        responseCreate = userClient.createUser(user);

        //получаем код респонса и боди
        int actualStatusCode = responseCreate.extract().statusCode();
        boolean actualIsUserCreated = responseCreate.extract().path("success");
        String actualResponseErrorMessage = responseCreate.extract().path("message");

        //задаем ожидаемый статус и боди
        int expectedStatusCode = 403;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "Email, password and name are required fields";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
        assertEquals(expectedBoolean, actualIsUserCreated);

    }
}
