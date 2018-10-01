package com.aurasoftwareinc.java.challenge1.mapper;

import java.lang.reflect.Field;

/**
 * Created by Minh LEE on 1/10/18.
 * demo Ltd
 * minhle.cse@gmail.com
 */
interface MappingAction {

    public Object registerTypeObject(Field field, Object mappingObject);

}
