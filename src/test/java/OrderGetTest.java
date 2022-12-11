import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.clients.OrderClient;
import ru.yandex.clients.UserClient;
import ru.yandex.objects.Ingredient;
import ru.yandex.objects.Order;
import ru.yandex.objects.User;
import ru.yandex.testdata.OrderGenerator;
import ru.yandex.testdata.UserGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DisplayName("Тесты на получение заказа")
public class OrderGetTest {

    private OrderClient orderClient;
    private Order order;
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

    @DisplayName("Получение ордеров с авторизацией")
    @Test
    public void getOrdersWithAuth() {
        //создаем два ордера
        orderClient.createOrder(order, token);
        order = OrderGenerator.getDefaultOrder();
        orderClient.createOrder(order, token);

        ValidatableResponse response = orderClient.getOrders(token);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Ingredient> orders = new ArrayList<>();
        orders = jsonPath.getList("orders", Ingredient.class);

        //получаем код респонса и боди
        boolean actual = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();

        int expectedStatusCode = 200;
        boolean expectedBoolean = true;

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actual);
        assertEquals(2, orders.size());
    }

    @DisplayName("Получение ордеров без авторизациии")
    @Test
    public void getOrdersWithoutAuth() {
        ValidatableResponse response = orderClient.getOrdersWithoutAuth();

        //получаем код респонса и боди
        boolean actual = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();
        String actualResponseErrorMessage = response.extract().path("message");

        int expectedStatusCode = 401;
        boolean expectedBoolean = false;
        String expectedResponseErrorMessage = "You should be authorised";

        //проверяем соотвествие
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBoolean, actual);
        assertEquals(expectedResponseErrorMessage, actualResponseErrorMessage);
    }
}
