package com.mattih.controlirrigtion.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.mattih.controlirrigtion.R;
import com.mattih.controlirrigtion.activities.DisplayGraphically;
import com.mattih.controlirrigtion.activities.NetworkActivity;
import com.mattih.controlirrigtion.activities.OnNewDataInsertedListener;
import com.mattih.controlirrigtion.adapter.BaseDataAdapter;
import com.mattih.controlirrigtion.api.RetrofitClient;
import com.mattih.controlirrigtion.data.MyStorage;
import com.mattih.controlirrigtion.models.BaseData;
import com.mattih.controlirrigtion.models.BaseDataResponse;
import com.mattih.controlirrigtion.models.SensorData;
import com.mattih.controlirrigtion.models.SensorsResponse;
import com.mattih.controlirrigtion.utilities.BaseUtils;
import com.mattih.controlirrigtion.utilities.NetworkUtilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MonitorFragment extends Fragment implements OnNewDataInsertedListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    TextView textViewDisplayUpdate;
    AppCompatButton buttonSeeGraphically;
    Thread thread;
    private String TAG = "com.mattih.controlirrigtion.ui.main;";
    private CircleProgress circleProgressSoil, circleProgressTemp, circleProgressHumid;
    private RecyclerView recyclerViewBaseData;
    private BaseDataAdapter baseDataAdapter;
    private NetworkActivity networkActivity;

    public static MonitorFragment newInstance(int index) {
        MonitorFragment fragment = new MonitorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void fetchBaseData() {
        Call<BaseDataResponse> call = RetrofitClient.getInstance(BaseUtils.BASE_URL1).getMyApi().getBaseData();

        call.enqueue(new Callback<BaseDataResponse>() {
            @Override
            public void onResponse(Call<BaseDataResponse> call, Response<BaseDataResponse> response) {
                Log.d(TAG, "onResponse = " + response.body().getBaseData());

                if (response.body() != null) {
                    ArrayList<BaseData> baseData = (ArrayList<BaseData>) response.body().getBaseData();
                    MyStorage.getPreference(getActivity()).saveBaseData(baseData);
                    displayBaseDataUpdate(baseData);
                }
            }

            @Override
            public void onFailure(Call<BaseDataResponse> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                displayBaseDataUpdate(MyStorage.getPreference(getActivity()).loadBaseData());
            }
        });
    }

    private void displayBaseDataUpdate(ArrayList<BaseData> baseData) {
        baseDataAdapter.setDataList(baseData);
    }

    private void fetchSensorData() {
        Call<SensorsResponse> call = RetrofitClient.getInstance(BaseUtils.BASE_URL1).getMyApi().getSensors();

        call.enqueue(new Callback<SensorsResponse>() {
            @Override
            public void onResponse(Call<SensorsResponse> call, Response<SensorsResponse> response) {
                Log.d(TAG, "onResponse = " + response.body().getDatas());
                if (response.body() != null) {
                    List<SensorData> dataList = response.body().getDatas();

                    MyStorage.getPreference(getActivity()).saveSensorData(dataList);
                    displayUpdate(dataList);
                }
            }

            @Override
            public void onFailure(Call<SensorsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure = ", t);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                displayUpdate(MyStorage.getPreference(getActivity()).loadSensorResponse());
            }
        });

    }

    private void displayUpdate(List<SensorData> dataList) {
        if (dataList != null) {
            SensorData lastData = dataList.get(dataList.size() - 1);
            // textViewDisplayResult.setText(String.valueOf(lastData.getSoilMoisture()));
            circleProgressSoil.setProgress(lastData.getSoilMoisture().intValue());
            circleProgressTemp.setProgress(lastData.getTemperature().intValue());
            circleProgressHumid.setProgress(lastData.getHumidity().intValue());

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        networkUtilities.getApiService().getSensors()
//                .enqueue(new Callback<List<SensorData>>() {
//                    @Override
//                    public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
//                        Log.d(TAG,"log-----------------");
//                        Log.d(TAG,"onResponce = "+response.body());
//
//                        if(response.raw().networkResponse() != null)
//                        {
//                            Log.d(TAG,"Response is from Network");
//
//                        }
//                        else if(response.raw().cacheResponse() != null)
//                        {
//                            Log.d(TAG,"Response is from Cache");
//
//                        }
//
//                        Log.d(TAG,"Response =="+response.body());
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<SensorData>> call, Throwable throwable) {
//
//                    }
//                });
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_monitor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circleProgressSoil = view.findViewById(R.id.circle_progress1);
        circleProgressTemp = view.findViewById(R.id.circle_progress2);
        circleProgressHumid = view.findViewById(R.id.circle_progress);
        buttonSeeGraphically = view.findViewById(R.id.btn_seeGraphically);

        textViewDisplayUpdate = view.findViewById(R.id.tv_updatedDate);
        buttonSeeGraphically.setOnClickListener(v -> {
            Intent intentSeeGraph = new Intent(getActivity(), DisplayGraphically.class);
            startActivity(intentSeeGraph);
        });


        recyclerViewBaseData = view.findViewById(R.id.rv_baseData);

        recyclerViewBaseData.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewBaseData.setHasFixedSize(true);
        recyclerViewBaseData.setItemAnimator(new DefaultItemAnimator());
        networkActivity = new NetworkActivity();

        baseDataAdapter = new
                BaseDataAdapter(MyStorage.getPreference(getContext()).loadBaseData(), getActivity());
        recyclerViewBaseData.setAdapter(baseDataAdapter);
//         AsyncTask task = new AsyncTask<Void,Void,Void>(){
//
//             @Override
//             protected Void doInBackground(Void... voids) {
//                 setupThread();
//                 return null;
//             }
//         };
//
//        new Handler().post(() -> {
//            while (true) {
//                //if (isOnline()){
//                if (NetworkUtilities.getInstance(getActivity()).hasConnection()) {
//                    fetchSensorData();
//                    fetchBaseData();
//                }
//                if (MyStorage.getPreference(getContext()).getLastUpdated() != null) {
//                   // float upadateAgo = System.currentTimeMillis() - MyStorage.getPreference(getContext()).getLastUpdated();
//                    textViewDisplayUpdate.setText(String.format("Updated : %s", new Date(MyStorage.getPreference(getContext()).getLastUpdated())));
//                }
//                //}
//                try {
//                    Thread.sleep(6000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        if (!NetworkUtilities.getInstance(getActivity()).hasConnection()) {

            displayUpdate(MyStorage.getPreference(getContext()).loadSensorResponse());

        } else {
            setupThread();
        }
    }

    private void setupThread() {
        if (thread == null) {
            thread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        //if (isOnline()){
                        if (NetworkUtilities.getInstance(getActivity()).hasConnection()) {
                            fetchSensorData();
                            fetchBaseData();
                        }
                        if (MyStorage.getPreference(getContext()).getLastUpdated() != -1) {
                            updateDate();
                        }
                        //}
                        try {
                            sleep(6000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };


            thread.start();
        }
    }

    @Override
    public void onDestroy() {
//        if(thread.isAlive()) {
        thread.interrupt();
        thread = null;

        super.onDestroy();
    }

    @Override
    public void onStop() {
        if (thread.isAlive()) {
            thread.interrupt();
        }
        thread = null;
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (thread.isAlive()) {
            thread.interrupt();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (thread.isInterrupted())
            thread.start();
    }

    private void updateDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        long upadateAgo = MyStorage.getPreference(getContext()).getLastUpdated();
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - upadateAgo;
        Log.d(TAG, "diff =" + diff);
        //Toast.makeText(getContext(),""+diff,Toast.LENGTH_SHORT).show();
        if (diff < (3600000)) {
            long l = diff / (60 * 1000);
            if (l == 0)
                textViewDisplayUpdate.setText("Updated : just now");
            else
                textViewDisplayUpdate.setText(String.format("Updated : %s min ago", l));


        } else if (diff < 86400000) {
            textViewDisplayUpdate.setText(String.format("Updated : %s hr ago", diff / (1000 * 60 * 60)));

        } else
            textViewDisplayUpdate.setText(String.format("Updated : %s", BaseUtils.getSimply(upadateAgo, "MMMM dd, yyyy")));
    }


    @Override
    public void onNewDataInserted(boolean status) {

    }
}