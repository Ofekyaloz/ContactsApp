package com.example.contactsapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactsapp.AppDB;
import com.example.contactsapp.R;
import com.example.contactsapp.daos.ContactDao;
import com.example.contactsapp.entities.Contact;

public class AddContactActivity extends AppCompatActivity {

    private ContactDao contactDao;
    private EditText etContactName;
    private EditText etPhoneNumber;
    private Contact contact;
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        AppDB db = AppDB.getDBInstance(getApplicationContext());
        contactDao = db.contactDao();
        etContactName = findViewById(R.id.contactNameField);
        etPhoneNumber = findViewById(R.id.contactPhoneNumberField);
        Button btnSave = findViewById(R.id.btnSave);

        if (getIntent().getExtras() != null) {
            userid = getIntent().getExtras().getInt("userid");
            contact = contactDao.get(getIntent().getExtras().getInt("id"));
            etContactName.setText(contact.getName());
            etPhoneNumber.setText(contact.getNumber());
        }

        btnSave.setOnClickListener(view -> {
            if (contact != null) {
                contact.setName(etContactName.getText().toString());
                contact.setNumber(etPhoneNumber.getText().toString());
                contactDao.update(contact);

            } else {
                Contact contact = new Contact(etContactName.getText().toString(), etPhoneNumber.getText().toString(), userid);
                contactDao.insert(contact);
            }
            finish();
        });
    }
}