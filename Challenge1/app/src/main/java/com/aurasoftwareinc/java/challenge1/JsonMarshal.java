package com.aurasoftwareinc.java.challenge1;

import android.util.Log;

import com.aurasoftwareinc.java.challenge1.mapper.MappingAction;
import com.aurasoftwareinc.java.challenge1.mapper.MappingActionByteObjectArray;
import com.aurasoftwareinc.java.challenge1.mapper.MappingActionByteArrayType;
import com.aurasoftwareinc.java.challenge1.mapper.MappingActionJsonMarshalType;
import com.aurasoftwareinc.java.challenge1.mapper.MappingActionJsonType;
import com.aurasoftwareinc.java.challenge1.mapper.MappingActionPrimitiveType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class JsonMarshal
{
    private static final String TAG = JsonMarshal.class.getName();

    public static Map<Class<?>, MappingAction> MAP_ACTIONS = new HashMap<>();

    /**
     * Register a marshal type. A each class need to have a different marshal action
     * A new class support need to implement a specific action.
     */
    public static void registerTypesIfNeeded(){

        if(MAP_ACTIONS.isEmpty()){
            //region byte types
            MAP_ACTIONS.put(byte[].class, new MappingActionByteArrayType());
            MAP_ACTIONS.put(Byte[].class, new MappingActionByteObjectArray());
            //endregion

            //region primitive type
            MAP_ACTIONS.put(byte.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(short.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(int.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(long.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(float.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(double.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(boolean.class, new MappingActionPrimitiveType());
            //endregion

            //region primitive object type
            MAP_ACTIONS.put(Byte.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(Short.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(Integer.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(Long.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(Float.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(Double.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(String.class, new MappingActionPrimitiveType());
            MAP_ACTIONS.put(Boolean.class, new MappingActionPrimitiveType());
            //endregion

            //region JSON object type
            MAP_ACTIONS.put(JSONObject.class, new MappingActionJsonType());
            MAP_ACTIONS.put(JSONArray.class, new MappingActionJsonType());
            //endregion

            //region JsonMarshalInterface type
            MAP_ACTIONS.put(JSONTypes.class, new MappingActionJsonMarshalType());
            MAP_ACTIONS.put(PrimitiveTypes.class, new MappingActionJsonMarshalType());
            MAP_ACTIONS.put(SubclassTypes.class, new MappingActionJsonMarshalType());
            MAP_ACTIONS.put(ObjectTypes.class, new MappingActionJsonMarshalType());
            //endregion
        }

    }

    /**
     * Marshal a object by applying marshal for all its fields. Support fields type: primitive, JSONObject
     * , JSONArrays and implemented JavaMarshalInterface. Ignore null field values.
     * @param object The object to be marshaled
     * @return A JSONObject marshal from the given object.
     */
    public static JSONObject marshalJSON(Object object)
    {
        registerTypesIfNeeded();

        JSONObject json = new JSONObject();

        try
        {
            for (Field field: object.getClass().getDeclaredFields()) {
                MappingAction action = (MappingAction) MAP_ACTIONS.get(field.getType());
                json.put(field.getName(), action.marshalAction(field,object));
            }
        }
        catch (Exception ignore)
        {
            Log.d(TAG,ignore.getLocalizedMessage());
        }

        return json;
    }

    /**
     * Parsing a JSONObject to fill up all fields for an input object
     * , JSONArrays and implemented JavaMarshalInterface.
     * @param object The object having fields to be filled with JSONObject values.
     * @param json The JSONObject to be parsed.
     * @return A parsing result - True if successful, otherwise false.
     */
    public static boolean unmarshalJSON(Object object, JSONObject json)
    {
        registerTypesIfNeeded();

        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                MappingAction action = (MappingAction) MAP_ACTIONS.get(field.getType());
                boolean backupAccessibleValue = field.isAccessible();
                field.setAccessible(true);
                field.set(object, action.unmarshalAction(field, json));
                field.setAccessible(backupAccessibleValue);

            }
        } catch (IllegalAccessException exception) {
            Log.d(TAG,exception.getLocalizedMessage());
        }
        return true;
    }
}