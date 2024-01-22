package com.example.contactsapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.contactsapp.R;

public class SettingsActivity extends AppCompatActivity {

    private boolean visibilityOfName;
    private boolean visibilityOfNumber;
    private boolean visibilityOfBirthday;
    private boolean visibilityOfGender;
    private String username;
    private boolean[] settings;
    private TextView settingsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        TextView tvUsername = findViewById(R.id.userNameTextView);
        if (getIntent() != null) {
            username = getIntent().getExtras().getString("username");
            tvUsername.setText(username);
            settings = getIntent().getExtras().getBooleanArray("settings");
        }

        SwitchCompat contactNameSwitch = findViewById(R.id.contactNameSwitch);
        SwitchCompat contactNumberSwitch = findViewById(R.id.contactNumberSwitch);
        SwitchCompat birthdaySwitch = findViewById(R.id.birthdaySwitch);
        SwitchCompat genderSwitch = findViewById(R.id.genderSwitch);
        visibilityOfName = settings[0];
        contactNameSwitch.setChecked(visibilityOfName);
        visibilityOfNumber = settings[1];
        contactNumberSwitch.setChecked(visibilityOfNumber);
        visibilityOfBirthday = settings[2];
        birthdaySwitch.setChecked(visibilityOfBirthday);
        visibilityOfGender = settings[3];
        genderSwitch.setChecked(visibilityOfGender);
        settingsError = findViewById(R.id.settings_tvError);

        contactNameSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> visibilityOfName = isChecked);

        contactNumberSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> visibilityOfNumber = isChecked);

        birthdaySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> visibilityOfBirthday = isChecked);

        genderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> visibilityOfGender = isChecked);

        Button btnSave = findViewById(R.id.btnSaveSettings);
        btnSave.setOnClickListener(view -> {
            if (!visibilityOfName && !visibilityOfNumber && !visibilityOfBirthday && !visibilityOfGender) {
                settingsError.setVisibility(View.VISIBLE);
                return;
            }
            settings = new boolean[]{visibilityOfName, visibilityOfNumber, visibilityOfBirthday, visibilityOfGender};
            int n = settings.length;
            SharedPreferences sharedPref = getSharedPreferences(username, Context.MODE_PRIVATE);
            String[] settingOptions = getResources().getStringArray(R.array.settings_options);
            SharedPreferences.Editor editor = sharedPref.edit();
            for (int i = 0; i < n; i++) {
                editor.putBoolean(settingOptions[i], settings[i]);
            }
            editor.apply();
            finish();
        });

        Button btnCancel = findViewById(R.id.btnCancelSettings);
        btnCancel.setOnClickListener(view -> finish());
    }
}