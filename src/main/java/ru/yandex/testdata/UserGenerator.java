package ru.yandex.testdata;

import ru.yandex.objects.User;

import java.util.Random;

public class UserGenerator {

    public static User getDefaultUser() {
        return new User("nasty" + new Random().nextInt(100000) + "@yandex.ru", "1234" + new Random().nextInt(100000), "name");
    }
}
