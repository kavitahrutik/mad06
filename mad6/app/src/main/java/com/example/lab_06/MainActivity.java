package com.example.lab_06;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String pn, mess;
    Button send_sms;
    ImageButton ib;
    EditText na, num, message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent pickcontact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        Toast t = new Toast(this);
        na = findViewById(R.id.name);
        num = findViewById(R.id.Number);
        message = findViewById(R.id.message);
        ib = findViewById(R.id.ib);
        send_sms = findViewById(R.id.Send_sms);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickcontact, 1);
            }
        });
        requestContactsPermission();
        send_sms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pn = num.getText().toString();
                mess = message.getText().toString();
                if (pn.equalsIgnoreCase("") || mess.equalsIgnoreCase("")) {
                    t.setText("phone number or message is empty");
                    t.setDuration(Toast.LENGTH_SHORT);
                    t.show();
                    na.requestFocus();
                } else {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(pn, null, mess, null, null);
                    t.setText("SMS Sent");
                    t.setDuration(Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
    }
    private void requestContactsPermission() {
        if (!hasContactsPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
        if (!hasSendSMSPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }
    private boolean hasContactsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                ==
                PackageManager.PERMISSION_GRANTED;
    }
    private boolean hasSendSMSPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED;
    }
    @SuppressLint({"SuspiciousIndentation", "Range"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Uri contactUri = data.getData();
        Cursor cursor = this.getContentResolver().query(contactUri,
                new String[]{"display_name","data1"}, null, null, null);
        try {
            if (cursor.getCount() == 0) return;
            cursor.moveToFirst();
            na.setText(cursor.getString(0));
            num.setText(cursor.getString(1));
        } finally {
            System.out.println("in phone cursor");
        }
    }
}