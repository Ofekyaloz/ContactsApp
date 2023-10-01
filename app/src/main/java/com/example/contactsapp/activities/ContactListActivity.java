package com.example.contactsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactsapp.AppDB;
import com.example.contactsapp.R;
import com.example.contactsapp.daos.ContactDao;
import com.example.contactsapp.entities.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private ContactDao contactDao;
    private List<Contact> contacts;
    private ArrayAdapter<Contact> adapter;
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        userid = getIntent().getExtras().getInt("userid");

        AppDB db = AppDB.getDBInstance(getApplicationContext());

        contactDao = db.contactDao();
        FloatingActionButton btnAddContact = findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            intent.putExtra("userid", userid);
            startActivity(intent);
        });

        contacts = new ArrayList<>();

        ListView lvContacts = findViewById(R.id.lvContacts);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);

        lvContacts.setAdapter(adapter);

        lvContacts.setOnItemLongClickListener(((adapterView, view, i, l) -> {
            Contact contact = contacts.remove(i);
            contactDao.delete(contact);
            adapter.notifyDataSetChanged();
            return true;
        }));

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
        adapter.notifyDataSetChanged();
    }
}