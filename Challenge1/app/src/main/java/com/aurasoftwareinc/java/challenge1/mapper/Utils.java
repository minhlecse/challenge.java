package com.aurasoftwareinc.java.challenge1.mapper;

/**
 * Created by Minh LEE on 2/10/18.
 * demo Ltd
 * minhle.cse@gmail.com
 */
public class Utils {

    /**
     * Util function to convert Byte object array to a primitive byte array.
     * @param byteArrayObject The object which can be extracted the field value
     * @return A byte[] converted or null if input is null
     */
    public static byte[] convertByteArrayObjectToByteArray(Byte[] byteArrayObject){
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

    /**
     * Util function to convert primitive byte array to Byte object array.
     * @param byteArray The object which can be extracted the field value
     * @return A Byte[] converted or null if input is null
     */
    public static Byte[] convertByteArrayToByteArrayObject(byte[] byteArray) {
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
