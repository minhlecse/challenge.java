package com.aurasoftwareinc.java.challenge1.mapper;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by Minh LEE on 1/10/18.
 * demo Ltd
 * minhle.cse@gmail.com
 */
public class MappingActionPrimitiveType implements MappingAction {

    private final String TAG = MappingActionPrimitiveType.class.getName();

    /**
     * Action to marshal a primitive value from the passing Object
     * @param field The field to be marshaled
     * @param mappingObject The object which can be extracted the field value
     * @return A ready Object to add to JSONObject value or null if passing field is null
     */
    @Override
    public Object marshalAction(Field field, Object mappingObject) {
        boolean backupAccessibleValue = field.isAccessible();
        try {
            field.setAccessible(true);
            return field.get(mappingObject);
        } catch (IllegalAccessException e) {
            Log.d(TAG,e.getLocalizedMessage());
        }
        finally {
            field.setAccessible(backupAccessibleValue);
        }
        return null;
    }

    /**
     * Action to unmarshal a primitive type from JSONObject to a primitive value
     * @param field The field to be unmarshaled
     * @param jsonObject The JSONObject which value to be unmarshed
     * @return An object with parsed value and relevant types or null if JSONObject doesn't contain any key as field name
     */
    @Override
    public Object unmarshalAction(Field field, JSONObject jsonObject) {
        try {
            String fieldName = field.getName();
            if(jsonObject.has(fieldName)){
                return jsonObject.get(fieldName);
            }
        } catch (JSONException e) {
            Log.d(TAG,e.getLocalizedMessage());
        }
        return null;
    }
}
