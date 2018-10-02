package com.aurasoftwareinc.java.challenge1.mapper;

import java.util.HashMap;

public class ActionHashMap<K,V> extends HashMap<K,V> {

    private static final String TYPE_MAPPING_NOT_FOUND = "No type mapping found. You need to implement Mapping Action and register for a new type";

    //
    // Throw no such of mapping action found exception if no map type found.
    //
    @Override
    public V get(Object key) {
        if(!this.containsKey(key)){
            throw new NoMappingException(TYPE_MAPPING_NOT_FOUND);
        }
        return super.get(key);
    }

    static class NoMappingException extends RuntimeException {

        public NoMappingException(String error) {
            super(error);
        }
    }

}
