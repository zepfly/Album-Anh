package com.example.jack_rambo.doanalbumanh;

import java.util.Comparator;
import java.util.HashMap;

public class MapComparator implements Comparator<HashMap<String, String>> {
    private final String key;
    private final String order;

    public MapComparator(String key, String order) {
        this.key = key;
        this.order = order;
    }

    @Override
    public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
        String value1 = o1.get(key);
        String value2 = o2.get(key);
        if (this.order.toLowerCase().contentEquals("asc"))
            return value1.compareTo(value2);
        else return value2.compareTo(value1);
    }
}
