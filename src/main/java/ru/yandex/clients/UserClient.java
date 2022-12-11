package ru.yandex.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.objects.User;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private static final String CREATE_PATH = "/api/auth/register";
    private static final String DELETE_UPDATE_PATH = "/api/auth/user";
    private static final String LOGIN_PATH = "/api/auth/login";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(CREATE_PATH)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .when()
                .delete(DELETE_UPDATE_PATH)
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(User user, String token) {
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .body(user)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Обновление пользователя")
    public ValidatableResponse updateUser(User user, String token) {
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .body(user)
                .when()
                .patch(DELETE_UPDATE_PATH)
                .then();
    }

    @Step("Обновление пользователя без авторизации")
    public ValidatableResponse updateUserWithoutAuth(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(DELETE_UPDATE_PATH)
                .then();
    }
}
