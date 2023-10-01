package com.example.contactsapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactsapp.entities.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM usersTable WHERE username = :username LIMIT 1")
    User get(String username);

    @Query("SELECT COUNT(*) FROM usersTable WHERE username = :username")
    int getUsername(String username);

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);
}
