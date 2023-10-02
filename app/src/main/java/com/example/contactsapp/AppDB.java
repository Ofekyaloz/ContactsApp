package com.example.contactsapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.contactsapp.daos.ContactDao;
import com.example.contactsapp.daos.UserDao;
import com.example.contactsapp.entities.Contact;
import com.example.contactsapp.entities.User;


@Database(entities = {Contact.class, User.class}, version = 5)
@TypeConverters(Converters.class)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract UserDao userDao();

    private static volatile AppDB INSTANCE;

    public static AppDB getDBInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(), AppDB.class, "ContactsApp")
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}
