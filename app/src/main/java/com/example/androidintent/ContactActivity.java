package com.example.androidintent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {

    public static final int REQUEST_SELECT_PHONE_NUMBER = 1;
    private String name;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Button contract = findViewById(R.id.select_contact);
        contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectContact(view);
            }
        });
        Button lifecycle = findViewById(R.id.lifecycle);
        lifecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, LifecycleActivity.class);
                startActivity(intent);
            }
        });
    }

    public void selectContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            assert data != null;
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            assert contactUri != null;
            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                this.name = cursor.getString(nameIndex);
                this.phoneNumber = cursor.getString(numberIndex);
            }
            assert cursor != null;
            cursor.close();
            TextView contactMessage = findViewById(R.id.contact_message);
            contactMessage.setText(String.format("%s %s", name, phoneNumber));
        }
    }
}