package com.aurasoftwareinc.java.challenge1.mapper;

import java.lang.reflect.Field;

/**
 * Created by Minh LEE on 1/10/18.
 * demo Ltd
 * minhle.cse@gmail.com
 */
public class MappingActionPrimitiveType implements MappingAction {

    @Override
    public Object registerTypeObject(Field field, Object mappingObject) {
        Field f = null;
        try {
            f = mappingObject.getClass().getDeclaredField(field.getName());
            f.setAccessible(true);
            return f.get(mappingObject);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
