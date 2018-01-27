package org.pispeb.treff_client.data.entities.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.HashSet;
import java.util.Set;

public class IntegerSetConverter {
    @TypeConverter
    public static Set<Integer> toSet(String string) {
        if (string == null) return null;
        if (string.length() == 0) return new HashSet<>();
        Set<Integer> set = new HashSet<>();
        String[] ints = string.split("#");
        for (String s : ints) {
            set.add(Integer.parseInt(s));
        }
        return set;
    }

    @TypeConverter
    public static String toString(Set<Integer> set) {
        if (set == null) return null;
        String string = "";
        for (Integer i : set) {
            string += i;
            string += "#";
        }
        return string;
    }
}
