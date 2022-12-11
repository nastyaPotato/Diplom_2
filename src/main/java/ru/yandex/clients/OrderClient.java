package ru.yandex.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.objects.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String PATH = "/api/orders";

    @Step("Создание ордера без авторизации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Создание ордера с авторизацией")
    public ValidatableResponse createOrder(Order order, String token) {
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .body(order)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Получение списка ордеров с авторизацией")
    public ValidatableResponse getOrders(String token) {
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .when()
                .get(PATH)
                .then();
    }

    @Step("Получение списка ордеров без авторизации")
    public ValidatableResponse getOrdersWithoutAuth() {
        return given()
                .spec(getSpec())
                .when()
                .get(PATH)
                .then();
    }
}
