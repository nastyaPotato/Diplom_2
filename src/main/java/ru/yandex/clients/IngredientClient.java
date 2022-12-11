package ru.yandex.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IngredientClient extends Client {

    private static final String PATH = "/api/ingredients";

    @Step("Получение ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getSpec())
                .when()
                .get(PATH)
                .then();
    }
}
