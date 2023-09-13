package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.repositories.UserRepository;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object objectToInject) {
        boolean wasPrivate =false;

        Field f = null;
        try {
            f = target.getClass().getDeclaredField(fieldName);
            if (!f.isEnumConstant()) {
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target,objectToInject);
            if(wasPrivate) {
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }

    }
}
