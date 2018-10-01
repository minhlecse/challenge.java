package com.aurasoftwareinc.java.challenge1.mapper;

import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by Minh LEE on 1/10/18.
 * demo Ltd
 * minhle.cse@gmail.com
 */
public interface MappingAction {

    /**
     * Action to marshal a field value to a new value which accepted by JSON
     * @param field The field to be marshaled
     * @param mappingObject The object which can be extracted the field value
     * @return A ready Object to add to JSONObject value or null if passing field is null
     */
    public Object marshalAction(Field field, Object mappingObject);

    /**
     * Action to unmarshal a field from JSONObject to object field with specific type
     * @param field The field to be unmarshaled
     * @param jsonObject The JSONObject which value to be unmarshed
     * @return An object with parsed value and relevant types or null if JSONObject doesn't contain any key as field name
     */
    public Object unmarshalAction(Field field, JSONObject jsonObject);


}
