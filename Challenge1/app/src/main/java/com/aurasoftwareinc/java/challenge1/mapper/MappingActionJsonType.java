package com.aurasoftwareinc.java.challenge1.mapper;

import android.util.Log;

import com.aurasoftwareinc.java.challenge1.JsonMarshalInterface;
import com.aurasoftwareinc.java.challenge1.ObjectTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by Minh LEE on 1/10/18.
 * demo Ltd
 * minhle.cse@gmail.com
 */
public class MappingActionJsonType implements MappingAction {

    private final String TAG = MappingActionJsonType.class.getName();

    /**
     * Action to marshal a JSONObject value from the passing Object
     * @param field The field to be marshaled
     * @param mappingObject The object which can be extracted the field value
     * @return A ready Object to add to JSONObject value or null if passing field is null
     */
    @Override
    public Object marshalAction(Field field, Object mappingObject) {
        boolean backupAccessibleValue = field.isAccessible();
        try {
            field.setAccessible(true);
            Object fieldObject = field.get(mappingObject);
            if(fieldObject != null) {
                return field.getType().cast(fieldObject);
            }
        } catch (IllegalAccessException e) {
            Log.d(TAG,e.getLocalizedMessage());
        }
        finally {
            field.setAccessible(backupAccessibleValue);
        }
        return null;
    }

    /**
     * Action to get a JSONObject or JSONArray from JSONObject to a specific object type
     * @param field The field to be unmarshaled
     * @param jsonObject The JSONObject which value to be unmarshed
     * @return An object with parsed value and relevant types or null if JSONObject doesn't contain any key as field name
     */
    @Override
    public Object unmarshalAction(Field field, JSONObject jsonObject) {
        try {
            String fieldName = field.getName();
            if(jsonObject.has(fieldName)){
                return field.getType().cast(jsonObject.get(fieldName));
            }
        } catch (JSONException e) {
            Log.d(TAG,e.getLocalizedMessage());
        }
        return null;
    }
}
