package com.aurasoftwareinc.java.challenge1;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JsonMarshal
{
    private static final String TAG = JsonMarshal.class.getName();
    public static final List<? extends Class<? extends Serializable>> PRIM_TYPES = Arrays.asList(byte.class, short.class, int.class, long.class, float.class, double.class, boolean.class, byte[].class);

    public static final List<? extends Class<? extends Serializable>> PRIM_OBJ_TYPES = Arrays.asList(Byte.class, Short.class, Integer.class, Long.class, Float.class,
            Double.class, Boolean.class, String.class, Byte[].class);



    public static JSONObject marshalJSON(Object object)
    {
        //
        // Todo: replace contents of this method with Your code.
        //

        JSONObject json = new JSONObject();

        try
        {
            for (Field field:
                    object.getClass().getDeclaredFields()) {

                Log.d("mtest","Field " + field.getName() + " type " + field.getGenericType() + "  " + JSONTypes.class.getGenericSuperclass());
                Field f = object.getClass().getDeclaredField(field.getName());
                f.setAccessible(true);
                if(byte[].class == field.getType()){
                    byte[] byteValue = (byte[]) f.get(object);
                    json.put(field.getName(), Base64.encodeToString(byteValue,Base64.DEFAULT));
                } else if(Byte[].class == field.getType()){
                    Byte[] byteValue = (Byte[]) f.get(object);
                    json.put(field.getName(), Base64.encodeToString(convertByteArrayObjectToByteArray(byteValue),Base64.DEFAULT));
                } else if(PRIM_TYPES.contains(field.getType()) || PRIM_OBJ_TYPES.contains(field.getType()) || JSONObject.class == field.getType() || JSONArray.class == field.getType()){
                    json.put(field.getName(),f.get(object));
                } else if(JsonMarshalInterface.class.isAssignableFrom(field.getType())){
                    JsonMarshalInterface marshalInterfaceObject = (JsonMarshalInterface) f.get(object);
                    //Recursive until get ready json
                    json.put(field.getName(),marshalInterfaceObject.marshalJSON());
                }
            }
        }
        catch (Exception ignore)
        {
            Log.d(TAG,ignore.getLocalizedMessage());
        }

        return json;
    }

    public static boolean unmarshalJSON(Object object, JSONObject json)
    {
        for (Field field:
                object.getClass().getDeclaredFields()) {
            try {
                Object obj = json.get(field.getName());
                boolean backupAccessibleValue = field.isAccessible();
                if(obj != null){
                    if(byte[].class == field.getType()) {
                        field.setAccessible(true);
                        field.set(object,Base64.decode(String.valueOf(obj),Base64.DEFAULT));
                    } else if(Byte[].class == field.getType()){
                        field.setAccessible(true);
                        field.set(object,convertByteArrayToByteArrayObject(Base64.decode(String.valueOf(obj),Base64.DEFAULT)));
                    } else if(PRIM_TYPES.contains(field.getType())){
                        Log.d("mtst", "umarshall  PRIM_TYPES");
                        field.setAccessible(true);
                        field.set(object,obj);
                    } else if(JSONObject.class == field.getType() || field.getType() == JSONArray.class || PRIM_OBJ_TYPES.contains(field.getType())){
                        Log.d("mtst", "umarshall  JSONObject");
                        field.setAccessible(true);
                        field.set(object,field.getType().cast(obj));
                    } else if(JsonMarshalInterface.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        JSONObject jsonObject = (JSONObject) obj;
                        JsonMarshalInterface jsonMarshalInterfaceObject = (JsonMarshalInterface) field.getType().newInstance();
                        jsonMarshalInterfaceObject.unmarshalJSON(jsonObject);
                        field.set(object,jsonMarshalInterfaceObject);
                    }
                }
                field.setAccessible(backupAccessibleValue);
            } catch (JSONException e) {
                Log.d("mtest",e.getLocalizedMessage());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    private static byte[] convertByteArrayObjectToByteArray(Byte[] byteArrayObject){
        if(byteArrayObject != null){
            int length = byteArrayObject.length;
            byte[] byteArray = new byte[length];
            for(int i = 0; i< length; i++){
                byteArray[i] = byteArrayObject[i].byteValue();
            }
            return byteArray;
        }
        return null;
    }
    private static Byte[] convertByteArrayToByteArrayObject(byte[] byteArray){
        if(byteArray != null){
            int length = byteArray.length;
            Byte[] byteArrayObj = new Byte[length];
            for(int i = 0; i< length; i++){
                byteArrayObj[i] = byteArray[i];
            }
            return byteArrayObj;
        }
        return null;
    }
}