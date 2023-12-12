package com.example.contactsapp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.contactsapp.AppDB;
import com.example.contactsapp.GenderResponse;
import com.example.contactsapp.R;
import com.example.contactsapp.api.WebServiceApi;
import com.example.contactsapp.daos.ContactDao;
import com.example.contactsapp.entities.Contact;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddContactActivity extends AppCompatActivity {

    private ContactDao contactDao;
    private EditText etContactName;
    private EditText etPhoneNumber;
    private Contact contact;
    private int userid;
    private EditText birthdayField;
    private String selectedDate = "";
    private Spinner genderSpinner;
    private String gender;

    private String photo = "";

    private ImageView ivContact;

    private Button btnDeletePhoto;

    private static final int PICK_IMAGE_REQUEST = 1; // You can use any unique value


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        AppDB db = AppDB.getDBInstance(getApplicationContext());
        contactDao = db.contactDao();
        etContactName = findViewById(R.id.contactNameField);
        etPhoneNumber = findViewById(R.id.contactPhoneNumberField);
        userid = getIntent().getExtras().getInt("userid");
        Button btnSave = findViewById(R.id.btnSave);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnDeletePhoto = findViewById(R.id.btnDeletePhoto);
        TextView tvError = findViewById(R.id.tv_addContactError);
        ivContact = findViewById(R.id.iv_contact);
        genderSpinner = findViewById(R.id.genderSpinner);

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

            photo = contact.getPhotoPath();
            if (!photo.isEmpty()) {
                Uri photoUri = Uri.parse(photo);
                Glide.with(this).load(photoUri).into(ivContact);
                ivContact.setVisibility(View.VISIBLE);
                btnDeletePhoto.setEnabled(true);
            }
            else {
                btnDeletePhoto.setEnabled(false);
                ivContact.setVisibility(View.GONE);
            }

            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setEnabled(true);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setEnabled(true);
            btnSave.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit, 0);
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

        etContactName.addTextChangedListener(new TextWatcher() {
            private Timer timer = new Timer();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                timer.cancel();
                timer = new Timer();
                long DELAY = 2000;
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                String typedText = editable.toString();
                                findGender(typedText.toLowerCase());
                            }
                        },
                        DELAY);
            }
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
                contact.setPhotoPath(photo);
                contactDao.update(contact);

            } else {
                Contact newContact = new Contact(contactName, contactNumber, userid, photo);
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

        btnDeletePhoto.setOnClickListener(v -> {
            photo = "";
            btnDeletePhoto.setEnabled(false);
            ivContact.setVisibility(View.GONE);
        });


        Button btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnUploadPhoto.setOnClickListener(view -> {
            openGallery();
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            photo = Objects.requireNonNull(data.getData()).toString();
            Uri photoUri = Uri.parse(photo);
            Glide.with(this)
                    .load(photoUri)
                    .into(ivContact);
            ivContact.setVisibility(View.VISIBLE);
            btnDeletePhoto.setEnabled(true);
        }
    }

    private void findGender(String name) {
        new Thread(() -> {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.genderize.io/?name=")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            WebServiceApi webServiceApi = retrofit.create(WebServiceApi.class);
            Call<GenderResponse> call = webServiceApi.getGender(name);
            call.enqueue(new Callback<GenderResponse>() {
                @Override
                public void onResponse(Call<GenderResponse> call, Response<GenderResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String gender = response.body().getGender();
                        if (gender == null) {
                            return;
                        }
                        if (gender.equals("male")) {
                            genderSpinner.setSelection(1);
                        } else if (gender.equals("female")) {
                            genderSpinner.setSelection(2);
                        } else {
                            genderSpinner.setSelection(0);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GenderResponse> call, Throwable t) {
                    Log.e("API Call", "Failed: " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }).start();
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
            tvError.setText(R.string.tv_contactname_min_length);
            return false;
        }
        if (contactName.length() > 20) {
            tvError.setText(R.string.tv_contactname_max_length);
            return false;
        }
        if (!Pattern.matches(getString(R.string.contact_name_regex), contactName)) {
            tvError.setText(R.string.tv_contactname_only_letters);
            return false;
        }
        if (!Pattern.matches(getString(R.string.phone_number_validation), contactNumber)) {
            tvError.setText(R.string.tv_invalid_phone_number);
            return false;
        }
        return true;
    }
}