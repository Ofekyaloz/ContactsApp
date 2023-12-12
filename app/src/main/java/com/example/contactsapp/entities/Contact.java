package com.example.contactsapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "contactsTable",
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "userid",
                childColumns = "userid", onDelete = ForeignKey.CASCADE))
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private final int userid;
    @NonNull
    private String name;
    @NonNull
    private String number;
    private String gender;
    private String birthday;
    private String photoPath;

    public Contact(@NonNull String name, @NonNull String number, int userid, String photoPath) {
        this.name = name;
        this.number = number;
        this.userid = userid;
        this.birthday = "";
        this.gender = "";
        this.photoPath = photoPath;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getUserid() {
        return userid;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

}
