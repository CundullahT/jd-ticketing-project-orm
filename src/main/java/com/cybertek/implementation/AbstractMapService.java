package com.cybertek.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapService<T,ID> {

    protected Map<ID,T> map = new HashMap<>();

    T save(ID id,T object){
        map.put(id,object);
        return object;
    }

    List<T> findAll(){
        return new ArrayList<>(map.values());
    }

    T findById(ID id){
        return map.get(id);
    }

    void deleteById(ID id){
        map.remove(id);
    }

    void delete(T object){

        map.entrySet().removeIf(entry -> entry.getValue().equals(object));
    }

    void update(ID id,T object){
//        map.entrySet().removeIf(entry -> entry.getValue().equals(object));  // Can use on other project's update
        map.put(id,object);
    }

}
