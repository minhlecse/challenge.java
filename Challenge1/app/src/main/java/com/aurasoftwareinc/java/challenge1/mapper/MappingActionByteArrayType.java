package com.aurasoftwareinc.java.challenge1.mapper;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by Minh LEE on 2/10/18.
 * demo Ltd
 * minhle.cse@gmail.com
 */
public class MappingActionByteArrayType implements MappingAction {

    private final String TAG = MappingActionByteArrayType.class.getName();

    /**
     * Action to marshal a byte[] value to a Base64String which accepted by JSONObject
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
                return Base64.encodeToString((byte[]) fieldObject, Base64.DEFAULT);
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
     * Action to unmarshal a Base64String from JSONObject to byte  array
     * @param field The field to be unmarshaled
     * @param jsonObject The JSONObject which value to be unmarshed
     * @return An object with parsed value and relevant types or null if JSONObject doesn't contain any key as field name
     */
    @Override
    public Object unmarshalAction(Field field, JSONObject jsonObject) {
        try {

            String fieldName = field.getName();
            if(jsonObject.has(fieldName)){
                Object fieldObj = jsonObject.get(fieldName);
                return Base64.decode(String.valueOf(fieldObj),Base64.DEFAULT);
            }
        } catch (JSONException e) {
            Log.d(TAG,e.getLocalizedMessage());
        }
        return null;
    }
}
