package com.aurasoftwareinc.java.challenge1.mapper;

import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by Minh LEE on 1/10/18.
 * demo Ltd
 * minhle.cse@gmail.com
 */
public class MappingActionJsonType implements MappingAction {
    @Override
    public Object registerTypeObject(Field field, Object mappingObject) {
        try {
            Field f  = mappingObject.getClass().getDeclaredField(field.getName());
            f.setAccessible(true);
            JSONObject jsonObject = (JSONObject)f.get(mappingObject);
            return jsonObject;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
