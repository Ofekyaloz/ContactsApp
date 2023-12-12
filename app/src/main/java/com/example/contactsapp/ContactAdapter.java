package com.example.contactsapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import com.example.contactsapp.entities.Contact;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private LayoutInflater inflater;
    private boolean[] settings;

    public ContactAdapter(Context ctx, List<Contact> userArrayList, boolean[] settings) {
        super(ctx, R.layout.contact_item, userArrayList);
        this.inflater = LayoutInflater.from(ctx);
        this.settings = settings;
    }

    public void setSettings(boolean[] settings) {
        this.settings = settings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Contact contact = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_item, parent, false);
        }

        TextView contactName = convertView.findViewById(R.id.contactNameTextView);
        if (settings[0]) {
            contactName.setText(contact.getName());
            contactName.setVisibility(View.VISIBLE);
        } else {
            contactName.setVisibility(View.GONE);
        }

        TextView phoneNumber = convertView.findViewById(R.id.contactPhoneNumberTextView);
        if (settings[1]) {
            phoneNumber.setText(contact.getNumber());
            phoneNumber.setVisibility(View.VISIBLE);
        } else {
            phoneNumber.setVisibility(View.GONE);
        }

        TextView birthdayTv = convertView.findViewById(R.id.contactBirthdayTextView);
        String birthday = contact.getBirthday();
        if (settings[2] && !birthday.isEmpty()) {
            birthdayTv.setVisibility(View.VISIBLE);
            birthdayTv.setText(birthday);
        } else {
            birthdayTv.setVisibility(View.GONE);
        }

        ImageView iv_gender = convertView.findViewById(R.id.iv_gender);
        TextView genderTv = convertView.findViewById(R.id.contactGenderTextView);
        String gender = contact.getGender();

        if (settings[3] && !gender.isEmpty()) {
            genderTv.setVisibility(View.VISIBLE);
            genderTv.setText(gender);
            if (gender.equals("Male")) {
                iv_gender.setImageResource(R.drawable.male_icon);
                iv_gender.setVisibility(View.VISIBLE);
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.blue));
            } else if (gender.equals("Female")) {
                iv_gender.setImageResource(R.drawable.female_icon);
                iv_gender.setVisibility(View.VISIBLE);
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.purple_200));
            } else {
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.green));
                iv_gender.setVisibility(View.INVISIBLE);
            }
        } else {
            genderTv.setVisibility(View.GONE);
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.green));
            iv_gender.setVisibility(View.INVISIBLE);
        }

        String photo = contact.getPhotoPath();
        if (!photo.isEmpty() && settings[3]) {
            Uri photoUri = Uri.parse(photo);
            Glide.with(getContext()).load(photoUri).into(iv_gender);
            iv_gender.setVisibility(View.VISIBLE);
        }


        return convertView;
    }
}
