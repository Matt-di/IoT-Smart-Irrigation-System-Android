package com.mattih.controlirrigtion.activities;

import android.os.Bundle;
import android.telephony.SmsManager;

import androidx.appcompat.app.AppCompatActivity;

import com.mattih.controlirrigtion.R;

public class SmsActivity extends AppCompatActivity {

    SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
    }
}
