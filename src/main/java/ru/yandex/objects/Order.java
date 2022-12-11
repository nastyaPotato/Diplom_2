package ru.yandex.objects;

import java.util.List;

public class Order {
    private List<Ingredient> ingredients;


    public Order(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

}
