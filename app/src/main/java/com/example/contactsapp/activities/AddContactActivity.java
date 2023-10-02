package com.example.contactsapp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactsapp.AppDB;
import com.example.contactsapp.R;
import com.example.contactsapp.daos.ContactDao;
import com.example.contactsapp.entities.Contact;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class AddContactActivity extends AppCompatActivity {

    private ContactDao contactDao;
    private EditText etContactName;
    private EditText etPhoneNumber;
    private Contact contact;
    private int userid;
    private EditText birthdayField;
    private String selectedDate = "";
    private String gender;

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
        Button btnCancel = findViewById(R.id.btnCancel);
        TextView tvError = findViewById(R.id.tv_addContactError);
        Spinner genderSpinner = findViewById(R.id.genderSpinner);


        birthdayField = findViewById(R.id.birthdayField);
        birthdayField.setOnClickListener(v -> {
            if (!selectedDate.isEmpty()) {
                showDatePickerDialog(selectedDate);
            } else {
                showDatePickerDialog("");
            }
        });
        birthdayField.setInputType(InputType.TYPE_NULL);


        contact = contactDao.get(getIntent().getExtras().getInt("id"));
        if (contact != null) {
            etContactName.setText(contact.getName());
            etPhoneNumber.setText(contact.getNumber());
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setEnabled(true);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setEnabled(true);
            selectedDate = contact.getBirthday();
            if (!selectedDate.isEmpty()) {
                birthdayField.setText(selectedDate);
            } else {
                birthdayField.setText(R.string.birthday_text);
            }
            String[] genderOptions = getResources().getStringArray(R.array.gender_options);
            String selectedGender = contact.getGender();
            int genderIndex = -1;
            for (int i = 0; i < genderOptions.length; i++) {
                if (genderOptions[i].equals(selectedGender)) {
                    genderIndex = i;
                    break;
                }
            }
            if (genderIndex != -1) {
                genderSpinner.setSelection(genderIndex);
            }
        }

        btnCancel.setOnClickListener(view -> finish());

        btnDelete.setOnClickListener(view -> {
            showDeleteConfirmationDialog();
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
                contact.setBirthday(selectedDate);
                contact.setGender(gender);
                contactDao.update(contact);

            } else {
                Contact newContact = new Contact(contactName, contactNumber, userid);
                newContact.setBirthday(selectedDate);
                newContact.setGender(gender);
                contactDao.insert(newContact);
            }
            finish();
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) {
                    gender = parentView.getItemAtPosition(position).toString();
                } else {
                    gender = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete " + contact.getName() + "?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    contactDao.delete(contact);
                    finish();
                })
                .setNegativeButton("No", (dialog, id) -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDatePickerDialog(String initialDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        if (!initialDate.isEmpty()) {
            String[] parts = initialDate.split("/");
            if (parts.length == 3) {
                dayOfMonth = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]) - 1;
                year = Integer.parseInt(parts[2]);
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                    birthdayField.setText(selectedDate);
                }, year, month, dayOfMonth);

        calendar.add(Calendar.YEAR, -120);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private boolean isValidInput(TextView tvError, String contactName, String contactNumber) {
        if (contactName.length() < 2) {
            tvError.setText("Contact Name must be two characters or longer!");
            return false;
        }
        if (contactName.length() > 20) {
            tvError.setText("Contact name is too long!\nPlease enter a name with 20 characters or fewer.");
            return false;
        }
        if (!Pattern.matches("[A-Za-z ]+", contactName)) {
            tvError.setText("Contact name must contain only letters!");
            return false;
        }
        if (!Pattern.matches("^[0-9]{10}$", contactNumber)) {
            tvError.setText("Invalid phone number!\n Please enter a 10-digit numeric phone number.");
            return false;
        }
        return true;
    }
}