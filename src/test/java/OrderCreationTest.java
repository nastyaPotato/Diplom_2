import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.clients.OrderClient;
import ru.yandex.clients.UserClient;
import ru.yandex.objects.Order;
import ru.yandex.objects.User;
import ru.yandex.testdata.OrderGenerator;
import ru.yandex.testdata.UserGenerator;

import static org.junit.Assert.assertEquals;

@DisplayName("Тесты на создание заказа")
public class OrderCreationTest {
    private OrderClient orderClient;
    private Order order;
    private ValidatableResponse responseCreate;
    private User user;
    private String token;
    private UserClient userClient;

    @Before
    public void setUp() throws InterruptedException {
        orderClient = new OrderClient();
        order = OrderGenerator.getDefaultOrder();
        userClient = new UserClient();
        user = UserGenerator.getDefaultUser();
        ValidatableResponse response = userClient.createUser(user);
        token = response.extract().path("accessToken");
        Thread.sleep(2000);
    }

    @After
    public void cleanUp() {
        if (token != null && !token.isEmpty()) {
            userClient.deleteUser(token);
        }
    }

    @DisplayName("Создание заказа без авторизации с ингридиентами")
    @Test
    public void orderCanBeCreatedWithoutAuthWithIngredients() {
        //получаем респонс
        responseCreate = orderClient.createOrderWithoutAuth(order);

        //получаем код респонса и боди
        boolean actual = responseCreate.extract().path("success");
        int actualStatusCode = responseCreate.extract().statusCode();

        int expectedStatusCode = 200;
        boolean expectedBoolean = true;

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actual);
    }

    @DisplayName("Создание заказа с авторизацией с ингридиентами")
    @Test
    public void orderCanBeCreatedWithAuthWithIngredients() {
        //получаем респонс
        responseCreate = orderClient.createOrder(order, token);

        //получаем код респонса и боди
        boolean actual = responseCreate.extract().path("success");
        int actualStatusCode = responseCreate.extract().statusCode();

        int expectedStatusCode = 200;
        boolean expectedBoolean = true;

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actual);
    }

    @DisplayName("Создание заказа с авторизацией без ингридиентов")
    @Test
    public void orderCanNotBeCreatedWithAuthWithoutIngredients() {
        order.setIngredients(null);
        //получаем респонс
        responseCreate = orderClient.createOrder(order, token);

        //получаем код респонса и боди
        boolean actual = responseCreate.extract().path("success");
        int actualStatusCode = responseCreate.extract().statusCode();
        String actualResponseErrorMessage = responseCreate.extract().path("message");

        int expectedStatusCode = 400;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "Ingredient ids must be provided";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actual);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
    }

    @DisplayName("Создание заказа без авторизации без ингридиентов")
    @Test
    public void orderCanNotBeCreatedWithoutAuthWithoutIngredients() {
        order.setIngredients(null);
        //получаем респонс
        responseCreate = orderClient.createOrderWithoutAuth(order);

        //получаем код респонса и боди
        boolean actual = responseCreate.extract().path("success");
        int actualStatusCode = responseCreate.extract().statusCode();
        String actualResponseErrorMessage = responseCreate.extract().path("message");

        int expectedStatusCode = 400;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "Ingredient ids must be provided";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actual);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
    }

    @DisplayName("Создание заказа с невалидным хэшем ингридиентов")
    @Test
    public void orderCanNotBeCreatedWithoutInvalidIngredientsHashcode() {
        Order order = OrderGenerator.getOrderWithInvalidIngredients();
        //получаем респонс
        responseCreate = orderClient.createOrder(order, token);

        //получаем код респонса и боди
        boolean actual = responseCreate.extract().path("success");
        int actualStatusCode = responseCreate.extract().statusCode();
        String actualResponseErrorMessage = responseCreate.extract().path("message");

        int expectedStatusCode = 400;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "One or more ids provided are incorrect";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actual);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
    }
}
