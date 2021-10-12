package ru.javaprojects.rewardcalculator;

import ru.javaprojects.rewardcalculator.model.User;
import ru.javaprojects.rewardcalculator.to.UserTo;
import ru.javaprojects.rewardcalculator.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static ru.javaprojects.rewardcalculator.DepartmentTestData.*;
import static ru.javaprojects.rewardcalculator.model.AbstractBaseEntity.START_SEQ;
import static ru.javaprojects.rewardcalculator.model.Role.*;

public class UserTestData {
    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;

    public static final String USER_MAIL = "user@yandex.ru";
    public static final String ADMIN_MAIL = "admin@gmail.com";

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Set.of(department1, department2), DEPARTMENT_HEAD);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Set.of(), ADMIN, DEPARTMENT_HEAD, ECONOMIST);

    public static User getNew() {
        return new User(null, "NewName", "new@gmail.com", "newPass", false, new Date(), new HashSet<>(), Collections.singleton(DEPARTMENT_HEAD));
    }

    public static User getNewWithManagedDepartments() {
        User newUser = getNew();
        newUser.addManagedDepartments(department1, department2);
        return newUser;
    }

    public static UserTo getUpdated() {
        return new UserTo(USER_ID, "UpdatedName", "update@gmail.com", false, Set.of(department3), Set.of(ADMIN, ECONOMIST));
    }

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}