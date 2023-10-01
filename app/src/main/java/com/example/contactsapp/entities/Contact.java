package com.example.contactsapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(
        tableName = "contactsTable",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "userid",
                childColumns = "userid",  // Update this to match the column name in the User entity
                onDelete = ForeignKey.CASCADE
        )
)
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private int userid;
    @NonNull
    private String name;
    @NonNull
    private String number;

    public Contact(@NonNull String name, @NonNull String number, int userid) {
        this.name = name;
        this.number = number;
        this.userid = userid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getNumber() {
        return number;
    }

    public void setNumber(@NonNull String number) {
        this.number = number;
    }

    @NonNull
    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                '}';
    }
}
