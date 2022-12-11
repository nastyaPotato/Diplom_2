package ru.yandex.testdata;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import ru.yandex.clients.IngredientClient;
import ru.yandex.objects.Ingredient;
import ru.yandex.objects.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class OrderGenerator {

    public static Order getDefaultOrder() {
        IngredientClient ingredientClient = new IngredientClient();
        ValidatableResponse response = ingredientClient.getIngredients();
        JsonPath jsonPath = response.extract().jsonPath();
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients = jsonPath.getList("data", Ingredient.class);

        Random rand = new Random();
        int randomSeriesLength = rand.nextInt(ingredients.size());
        List<Ingredient> randomSeries = new ArrayList<>();
        for (int i = 0; i < randomSeriesLength; i++) {
            int randomIndex = rand.nextInt(ingredients.size());
            randomSeries.add(ingredients.get(randomIndex));
        }
        return new Order(randomSeries);
    }

    public static Order getOrderWithInvalidIngredients() {
        String uuid = String.valueOf(UUID.randomUUID());
        uuid = uuid.replace("-", "");
        uuid = uuid.substring(0, 24);
        Ingredient ingredient = new Ingredient(uuid);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        return new Order(ingredients);
    }

}
