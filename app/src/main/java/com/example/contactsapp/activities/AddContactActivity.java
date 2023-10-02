package com.example.contactsapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactsapp.AppDB;
import com.example.contactsapp.R;
import com.example.contactsapp.daos.ContactDao;
import com.example.contactsapp.entities.Contact;

import java.util.regex.Pattern;

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
        userid = getIntent().getExtras().getInt("userid");
        Button btnSave = findViewById(R.id.btnSave);
        Button btnDelete = findViewById(R.id.btnDelete);

        TextView tvError = findViewById(R.id.tv_addContactError);

        contact = contactDao.get(getIntent().getExtras().getInt("id"));
        if (contact != null) {
            etContactName.setText(contact.getName());
            etPhoneNumber.setText(contact.getNumber());
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setEnabled(true);

        }

        btnDelete.setOnClickListener(view -> {
            contactDao.delete(contact);
            finish();
        });

        btnSave.setOnClickListener(view -> {
            String contactName = etContactName.getText().toString();
            String contactNumber = etPhoneNumber.getText().toString();
            if (!isValidInput(tvError, contactName, contactNumber)) {
                tvError.setVisibility(View.VISIBLE);
                return;
            }
            if (contact != null) {
                contact.setName(contactName);
                contact.setNumber(contactNumber);
                contactDao.update(contact);

            } else {
                Contact contact = new Contact(contactName, contactNumber, userid);
                contactDao.insert(contact);
            }
            finish();
        });
    }

    private boolean isValidInput(TextView tvError, String contactName, String contactNumber) {
        if (contactName.length() < 3) {
            tvError.setText("Contact Name must be 3 characters or longer!");
            return false;
        }
        if (contactName.length() > 20) {
            tvError.setText("Contact name is too long!\nPlease enter a name with 20 characters or fewer.");
            return false;
        }
        if (!Pattern.matches("[A-Za-z]+", contactName)) {
            tvError.setText("Contact name must contain at least one letter!");
            return false;
        }
        if (!Pattern.matches("^[0-9]{10}$", contactNumber)) {
            tvError.setText("Invalid phone number! Please enter a 10-digit numeric phone number.");
            return false;
        }
        return true;
    }
}