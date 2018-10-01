package com.aurasoftwareinc.java.challenge1.mapper;

import com.aurasoftwareinc.java.challenge1.JsonMarshalInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by Minh LEE on 2/10/18.
 * demo Ltd
 * minhle.cse@gmail.com
 */
public class MappingActionJsonMarshalType implements MappingAction {

    /**
     * Action to recursively marshal a JsonMarshalInterface value to a relevant value accepted by JSONObject
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
                JsonMarshalInterface marshalInterfaceObject = (JsonMarshalInterface) fieldObject;
                //Recursive until get ready json
                return marshalInterfaceObject.marshalJSON();
            }
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        finally {
            field.setAccessible(backupAccessibleValue);
        }
        return null;
    }

    /**
     * Action to recursively unmarshal a JsonMarshalInterface object from JSONObject to a specific object type
     * @param field The field to be unmarshaled
     * @param jsonObject The JSONObject which value to be unmarshed
     * @return An object with parsed value and relevant types or null if JSONObject doesn't contain any key as field name
     */
    @Override
    public Object unmarshalAction(Field field, JSONObject jsonObject) {
        try {

            String fieldName = field.getName();
            if(jsonObject.has(fieldName)){
                JSONObject fieldObject = (JSONObject) jsonObject.get(fieldName);
                JsonMarshalInterface jsonMarshalInterfaceObject = (JsonMarshalInterface) field.getType().newInstance();
                jsonMarshalInterfaceObject.unmarshalJSON(fieldObject);
                return jsonMarshalInterfaceObject;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
