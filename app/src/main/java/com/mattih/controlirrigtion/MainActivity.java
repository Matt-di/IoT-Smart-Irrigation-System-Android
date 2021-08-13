package com.mattih.controlirrigtion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mattih.controlirrigtion.activities.LoginActivity;
import com.mattih.controlirrigtion.activities.OnNewDataInsertedListener;
import com.mattih.controlirrigtion.activities.SettingsActivity;
import com.mattih.controlirrigtion.activities.SmsActivity;
import com.mattih.controlirrigtion.activities.WeatherActivity;
import com.mattih.controlirrigtion.adapter.SectionsPagerAdapter;
import com.mattih.controlirrigtion.data.MyStorage;

public class MainActivity extends AppCompatActivity implements OnNewDataInsertedListener {

    private String TAG = "com.mattih.controlirrigtion";
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.view_pager);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.toggleClose, R.string.toggleOpen);
        toggle.setDrawerSlideAnimationEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

            //actionBar.setIcon();
        }

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    navigationView.setCheckedItem(menuItem);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.nav_sms:
                    navigationView.setCheckedItem(menuItem);
                    intent = new Intent(getApplication(), SmsActivity.class);
                    drawerLayout.closeDrawers();
                    startActivity(intent);
                    return true;
                case R.id.nav_weather:
                    navigationView.setCheckedItem(menuItem);
                    intent = new Intent(getApplication(), WeatherActivity.class);
                    drawerLayout.closeDrawers();
                    startActivity(intent);
                    return true;
                case R.id.nav_logout: {
                    alertCreate();

                    return true;
                }
                default:
                    return true;
            }
        });


    }

    private void alertCreate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.Theme_AppCompat);
        AlertDialog d;
        builder.setTitle("Are you sure to Logout")
                .setPositiveButton("Yes", (dialog, which) -> {
                    MyStorage.getPreference(getApplicationContext()).logoutUser();
                    Intent intent1 = new Intent(getApplication(), LoginActivity.class);
                    startActivity(intent1);
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        d = builder.create();
        d.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawers();
                else
                    drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_setting:
                Intent intentSetting = new Intent(this, SettingsActivity.class);
                startActivity(intentSetting);
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }

    @Override
    public void onNewDataInserted(boolean status) {

    }
}
