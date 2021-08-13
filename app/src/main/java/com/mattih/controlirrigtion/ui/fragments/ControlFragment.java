package com.mattih.controlirrigtion.ui.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.mattih.controlirrigtion.R;
import com.mattih.controlirrigtion.activities.OnNewDataInsertedListener;
import com.mattih.controlirrigtion.api.RetrofitClient;
import com.mattih.controlirrigtion.data.MyStorage;
import com.mattih.controlirrigtion.models.LoginResponse;
import com.mattih.controlirrigtion.utilities.BaseUtils;
import com.mattih.controlirrigtion.utilities.NetworkUtilities;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mattih.controlirrigtion.utilities.NetworkUtilities.TAG;


public class ControlFragment extends Fragment implements View.OnClickListener, OnNewDataInsertedListener {


    private Button btnToggleMotor;
    private Button btnChangeTempTV;
    private Button btnChangeHumTV;
    private Button btnChangePhoneNum;
    private Button btnChangeLocation;

    private ImageView motorStatusImage;

    public ControlFragment() {
        // Required empty public constructor
    }

    public static ControlFragment newInstance() {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public void changeThreshold(String name) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        View mLayout = LayoutInflater.from(getActivity()).inflate(R.layout.myinputdialog, null);

        Button cancelBtn = mLayout.findViewById(R.id.btn_cancel);
        Button changeBtn = mLayout.findViewById(R.id.btn_change);

        EditText inputBoxEt = mLayout.findViewById(R.id.et_inputBox);

        builder.setView(mLayout);

        final AlertDialog myAlert = builder.create();
        myAlert.setCanceledOnTouchOutside(false);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlert.dismiss();
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inputBoxEt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please Enter correct value", Toast.LENGTH_SHORT).show();
                    if (name.equals("phoneNumber"))
                        if (inputBoxEt.getText().toString().length() < 10)
                            Toast.makeText(getActivity(), "Please, Enter correct phone Number", Toast.LENGTH_SHORT).show();
                        else
                            inputBoxEt.setError("Number between 1 an 100 required");
                } else {

                    toggleMotor(name, inputBoxEt.getEditableText().toString());
                    myAlert.dismiss();
                }
            }
        });

        myAlert.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_control, container, false);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnChangeSM = view.findViewById(R.id.btn_changeSM);
        btnToggleMotor = view.findViewById(R.id.btn_toggleMotor);
        Button btnChangeControlMode = view.findViewById(R.id.btn_changeControlMode);

        motorStatusImage = view.findViewById(R.id.img_motorStatus);
        ImageView controlModeImage = view.findViewById(R.id.img_controlMode);
        if (MyStorage.getPreference(getActivity()).loadBaseData() != null) {
            if (MyStorage.getPreference(getActivity()).loadBaseData().get(4).getValue().equals("0"))
                btnToggleMotor.setEnabled(false);
            motorStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_sprinklers_off));
        }
        btnToggleMotor.setOnClickListener(this);
        btnChangeControlMode.setOnClickListener(this);
        btnChangeSM.setOnClickListener(this);
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void toggleMotor(String name, String value) {

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("name", name);
        paramObject.addProperty("value", value);

        Call<LoginResponse> call = RetrofitClient.getInstance(BaseUtils.BASE_URL1).getMyApi().updateBaseData(paramObject);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                Log.d(TAG, "" + response);
                if (response.body() != null) {
                    LoginResponse response1 = response.body();
                    if (response1.getStatus()) {
                        if (name.equals("control_mode") && value.equals("0")) {
                            btnToggleMotor.setEnabled(false);
                            motorStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_sprinklers_off));

                        } else {
                            btnToggleMotor.setEnabled(true);
                            motorStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_sprinklers));

                        }

                        Toast.makeText(getActivity(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Some Error Occured \n" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (NetworkUtilities.getInstance(getActivity()).hasConnection()) {
            switch (v.getId()) {
                case R.id.btn_toggleMotor:
                    String val = MyStorage.getPreference(getActivity()).loadBaseData().get(3).getValue().equals("0") ? "1" : "0";
                    toggleMotor("motor_status", val);
                    break;
                case R.id.btn_changeControlMode:
                    String value = MyStorage.getPreference(getActivity()).loadBaseData().get(4).getValue().equals("0") ? "1" : "0";
                    toggleMotor("control_mode", value);
                    break;
                case R.id.btn_changeSM:
                    changeThreshold("soil_moistureTV");
                    break;
                case R.id.btn_changeHumidTV:
                    changeThreshold("humidityTV");
                    break;
                case R.id.btn_changeTV:
                    changeThreshold("temperatureTV");
                    break;
                case R.id.btn_changePhoneNumber:
                    changeThreshold("phoneNumber");
                default:
                    break;
            }
        } else {
            showToast("Check your Internet connection");

        }
    }


    private void changeMode() {

    }

    @Override
    public void onNewDataInserted(boolean status) {
        if (status) {
            btnToggleMotor.setEnabled(false);
        }
    }
}
