package com.info121.mycoach.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.info121.mycoach.AbstractActivity;
import com.info121.mycoach.App;
import com.info121.mycoach.R;
import com.info121.mycoach.api.RestClient;
import com.info121.mycoach.models.ObjectRes;
import com.info121.mycoach.services.SmartLocationService;
import com.info121.mycoach.utils.PrefDB;
import com.info121.mycoach.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AbstractActivity {

    Context mContext = LoginActivity.this;
    PrefDB prefDB;

    @BindView(R.id.user_name)
    EditText mUserName;

    @BindView(R.id.remember_me)
    CheckBox mRemember;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        prefDB = new PrefDB(getApplicationContext());

        if (prefDB.getBoolean(App.CONST_REMEMBER_ME)) {
            mUserName.setText(prefDB.getString(App.CONST_USER_NAME));
            mRemember.setChecked(true);
        }

    }

    @OnClick(R.id.login)
    public void loginOnClick(){
        mProgressBar.setVisibility(View.VISIBLE);
        callValidateDriver();
    }

    private void callValidateDriver(){
        Call<ObjectRes> call = RestClient.COACH().getApiService().ValidateDriver(mUserName.getText().toString().trim());

        call.enqueue(new Callback<ObjectRes>() {
            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {
                App.userName = mUserName.getText().toString();
                App.deviceID = Util.getDeviceID(getApplicationContext());
                App.authToken = response.body().getToken();
                App.timerDelay = 1000;

                callUpdateDevice();
            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                mUserName.setError("Wrong user name");
                mUserName.requestFocus();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void callUpdateDevice(){
        Log.e("====" , "=========================================");
        Log.e("DEVICE ID: " , Util.getDeviceID(getApplicationContext()));
        Log.e("DEVICE TYPE " , App.DEVICE_TYPE);
        Log.e("FCM TOKEN" , App.FCM_TOKEN);
        Log.e("====" , "=========================================");


        Call<ObjectRes> call = RestClient.COACH().getApiService().UpdateDevice(Util.getDeviceID(getApplicationContext()),
                App.DEVICE_TYPE,
                App.FCM_TOKEN);

        call.enqueue(new Callback<ObjectRes>() {
            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {
                // Add to Appication Varialbles

                loginSuccessful();
            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                Toast.makeText(mContext, "Getting error in firebase token request.", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loginSuccessful(){
        mProgressBar.setVisibility(View.GONE);


        // instantiate wiht new Token
        //RestClient.Dismiss();

        prefDB.putString(App.CONST_USER_NAME, App.userName);
        prefDB.putString(App.CONST_DEVICE_ID, App.deviceID);
        prefDB.putLong(App.CONST_TIMER_DELAY, App.timerDelay);

        // location
        startLocationService();

        if (mRemember.isChecked())
            prefDB.putBoolean(App.CONST_REMEMBER_ME, true);
        else
            prefDB.putBoolean(App.CONST_REMEMBER_ME, false);

        // login successful
        startActivity(new Intent(LoginActivity.this, JobOverviewActivity.class));
    }

    private void startLocationService() {
        if (isGPSEnabled()) {
            Intent serviceIntent = new Intent(LoginActivity.this, SmartLocationService.class);
            LoginActivity.this.startService(serviceIntent);
        }
    }

    private boolean isGPSEnabled() {

        mContext = LoginActivity.this;

        final LocationManager manager = (LocationManager) getSystemService(mContext.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            alertDialog.setTitle("GPS Settings");
            alertDialog.setMessage("Your GPS/Location service is off. \n Do you want to turn on location service?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();

            return false;
        } else
            return true;
    }
}
