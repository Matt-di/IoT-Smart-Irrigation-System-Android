package com.mattih.controlirrigtion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mattih.controlirrigtion.MainActivity;
import com.mattih.controlirrigtion.R;
import com.mattih.controlirrigtion.api.RetrofitClient;
import com.mattih.controlirrigtion.data.MyStorage;
import com.mattih.controlirrigtion.models.LoginResponse;
import com.mattih.controlirrigtion.utilities.BaseUtils;
import com.mattih.controlirrigtion.utilities.NetworkUtilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "com.mattih.controlirrig";
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    ProgressBar progressBar;
    TextView textViewExit;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MyStorage.getPreference(getApplicationContext()).checkLogin()) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);

        buttonLogin = findViewById(R.id.login);

        progressBar = findViewById(R.id.loading);

        buttonLogin.setOnClickListener(v -> {
            if (checkInput()) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                sendLogin();

            }
        });


        // getWindow().addFlags(Window.FEATURE_NO_TITLE);
    }


    private void sendLogin() {
        if (NetworkUtilities.getInstance(getApplicationContext()).hasConnection()) {
            String userEmail = editTextEmail.getEditableText().toString();
            String userPassword = editTextPassword.getEditableText().toString();

            Call<LoginResponse> call = RetrofitClient.getInstance(BaseUtils.BASE_URL1).getMyApi().userLogin(userEmail, userPassword);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse.getStatus()) {
                            showMessage(loginResponse.getMessage());
                            MyStorage.getPreference(getApplicationContext()).saveLogin(true);
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showMessage(loginResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure" + t.toString() + "");

                    showMessage("Sorry!, Some Error Occurred \n" + t.getLocalizedMessage());
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            showMessage("Sorry!, Connect to Internet ");
        }
    }

    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(editTextEmail.getText().toString()) || editTextEmail == null) {
            editTextEmail.setError(getResources().getString(R.string.invalid_username));
            return false;
        } else if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
            editTextPassword.setError(getResources().getString(R.string.invalid_password));
            return false;
        } else return true;
    }


}
