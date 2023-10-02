package com.example.contactsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactsapp.AppDB;
import com.example.contactsapp.ContactAdapter;
import com.example.contactsapp.Converters;
import com.example.contactsapp.R;
import com.example.contactsapp.daos.ContactDao;
import com.example.contactsapp.daos.UserDao;
import com.example.contactsapp.entities.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private ContactDao contactDao;
    private UserDao userDao;
    private List<Contact> contacts;
    private ContactAdapter adapter;
    private int userid;
    private String username;
    private boolean[] settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        AppDB db = AppDB.getDBInstance(getApplicationContext());
        contactDao = db.contactDao();
        userDao = db.userDao();

        userid = getIntent().getExtras().getInt("userid");
        username = getIntent().getExtras().getString("username");
        settings = Converters.toBooleanArray(userDao.get(username).getSettings());


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar);

            ImageButton backButton = actionBar.getCustomView().findViewById(R.id.backImageButton);
            backButton.setOnClickListener(v -> finish());

            ImageButton settingsButton = actionBar.getCustomView().findViewById(R.id.settingsImageButton);
            settingsButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("settings", settings);
                startActivity(intent);
            });
        }


        FloatingActionButton btnAddContact = findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            intent.putExtra("userid", userid);
            startActivity(intent);
        });

        contacts = new ArrayList<>();

        ListView lvContacts = findViewById(R.id.lvContacts);
        adapter = new ContactAdapter(this, contacts, getIntent().getExtras().getBooleanArray("settings"));

        lvContacts.setAdapter(adapter);
        lvContacts.setOnItemClickListener(((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            intent.putExtra("id", contacts.get(i).getId());
            intent.putExtra("userid", userid);
            startActivity(intent);
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        contacts.clear();
        contacts.addAll(contactDao.getContacts(userid));
        settings = Converters.toBooleanArray(userDao.get(username).getSettings());
        adapter.setSettings(settings);
        adapter.notifyDataSetChanged();
    }
}