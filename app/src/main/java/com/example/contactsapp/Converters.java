package com.example.contactsapp;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static String fromBooleanArray(boolean[] array) {
        StringBuilder builder = new StringBuilder();
        for (boolean b : array) {
            builder.append(b ? 'T' : 'F');
        }
        return builder.toString();
    }

    @TypeConverter
    public static boolean[] toBooleanArray(String settings) {
        boolean[] array = new boolean[settings.length()];
        for (int i = 0; i < settings.length(); i++) {
            array[i] = settings.charAt(i) == 'T';
        }
        return array;
    }
}
