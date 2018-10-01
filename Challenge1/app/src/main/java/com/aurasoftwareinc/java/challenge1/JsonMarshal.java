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
                if(byte[].class == field.getType()){
                    Field f = object.getClass().getDeclaredField(field.getName());
                    f.setAccessible(true);
                    byte[] byteValue = (byte[]) f.get(object);
                    json.put(field.getName(), Base64.encodeToString(byteValue,Base64.DEFAULT));
                } else if(PRIM_TYPES.contains(field.getType())){
                    Field f = object.getClass().getDeclaredField(field.getName());
                    f.setAccessible(true);
                    json.put(field.getName(),f.get(object));
                } else if(Byte[].class == field.getType()){
                    Field f = object.getClass().getDeclaredField(field.getName());
                    f.setAccessible(true);
                    Byte[] byteValue = (Byte[]) f.get(object);
                    json.put(field.getName(), Base64.encodeToString(convertByteArrayObjectToByteArray(byteValue),Base64.DEFAULT));
                } else if(PRIM_OBJ_TYPES.contains(field.getType())){
                    Field f = object.getClass().getDeclaredField(field.getName());
                    f.setAccessible(true);
                    json.put(field.getName(),f.get(object));
                }
                else if(JSONObject.class == field.getType()){
                    Field f = object.getClass().getDeclaredField(field.getName());
                    f.setAccessible(true);
                    JSONObject jsonObject = (JSONObject)f.get(object);
                    json.put(field.getName(),jsonObject);

                } else if(JSONArray.class == field.getType()){
                    Field f = object.getClass().getDeclaredField(field.getName());
                    f.setAccessible(true);
                    JSONArray jsonArray = (JSONArray)f.get(object);
                    json.put(field.getName(),jsonArray);
                }else if(JsonMarshalInterface.class.isAssignableFrom(field.getType())){
                    Log.d("mtest","mm ObjectTypes TYPE");
                    Field f = object.getClass().getDeclaredField(field.getName());
                    f.setAccessible(true);
                    JsonMarshalInterface marshalInterfaceObject = (JsonMarshalInterface) f.get(object);
                    json.put(field.getName(),marshalInterfaceObject.marshalJSON());
                }
            }
        }
        catch (Exception ignore)
        {
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
                    } else if(PRIM_OBJ_TYPES.contains(field.getType())){
                        Log.d("mtst", "umarshall  PRIM_OBJ_TYPES");
                        field.setAccessible(true);
                        field.set(object,obj);
                    }
                    else if(JSONObject.class == field.getType()){
                        Log.d("mtst", "umarshall  JSONObject");
                        field.setAccessible(true);
                        JSONObject jsonObject = (JSONObject) obj;
                        field.set(object,jsonObject);
                    } else if(field.getType() == JSONArray.class){
                        Log.d("mtst", "umarshall  JSONArray");
                        field.setAccessible(true);
                        JSONArray jsonArray = (JSONArray) obj;
                        field.set(object,jsonArray);
                    } else if(field.getType() == JSONTypes.class){
                        Log.d("mtst", "umarshall  JSONType");
                        field.setAccessible(true);
                        JSONObject jsonObject = (JSONObject) obj;
                        JSONTypes jsonTypes = new JSONTypes();
                        jsonTypes.unmarshalJSON(jsonObject);
                        field.set(object,jsonTypes);
                    } else if(field.getType() == PrimitiveTypes.class){
                        Log.d("mtst", "umarshall  PrimitiveTypes");
                        field.setAccessible(true);
                        JSONObject jsonObject = (JSONObject) obj;
                        PrimitiveTypes primitiveTypes = new PrimitiveTypes();
                        primitiveTypes.unmarshalJSON(jsonObject);
                        field.set(object,primitiveTypes);
                    } else if(field.getType() == ObjectTypes.class){
                        Log.d("mtst", "umarshall  PrimitiveTypes");
                        field.setAccessible(true);
                        JSONObject jsonObject = (JSONObject) obj;
                        ObjectTypes objectTypes = new ObjectTypes();
                        objectTypes.unmarshalJSON(jsonObject);
                        field.set(object,objectTypes);
                    }
                }
                field.setAccessible(backupAccessibleValue);
            } catch (JSONException e) {
                Log.d("mtest",e.getLocalizedMessage());
            } catch (IllegalAccessException e) {
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