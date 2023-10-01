package com.example.contactsapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactsapp.entities.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contactsTable WHERE userId = :userId")
    List<Contact> getContacts(int userId);

    @Query("SELECT * From contactsTable WHERE id = :id")
    Contact get(int id);

    @Insert
    void insert(Contact... contacts);

    @Update
    void update(Contact ... contacts);

    @Delete
    void delete(Contact ... contacts);
}
