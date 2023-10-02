package com.example.contactsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.contactsapp.entities.Contact;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    LayoutInflater inflater;

    public ContactAdapter(Context ctx, List<Contact> userArrayList) {
        super(ctx, R.layout.contact_item, userArrayList);
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Contact contact = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_item, parent, false);
        }

        TextView contactName = convertView.findViewById(R.id.contactNameTextView);
        TextView phoneNumber = convertView.findViewById(R.id.contactPhoneNumberTextView);

        contactName.setText(contact.getName());
        phoneNumber.setText(contact.getNumber());

        TextView genderTv = convertView.findViewById(R.id.contactGenderTextView);
        String gender = contact.getGender();
        if (!gender.isEmpty()) {
            genderTv.setVisibility(View.VISIBLE);
            genderTv.setText(gender);
        } else {
            genderTv.setVisibility(View.GONE);
        }
        TextView birthdayTv = convertView.findViewById(R.id.contactBirthdayTextView);
        String birthday = contact.getBirthday();
        if (!birthday.isEmpty()) {
            birthdayTv.setVisibility(View.VISIBLE);
            birthdayTv.setText(birthday);
        } else {
            birthdayTv.setVisibility(View.GONE);
        }

        return convertView;
    }
}
