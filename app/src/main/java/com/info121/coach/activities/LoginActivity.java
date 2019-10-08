package com.info121.coach.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.info121.coach.AbstractActivity;
import com.info121.coach.App;
import com.info121.coach.R;
import com.info121.coach.api.RestClient;
import com.info121.coach.models.ObjectRes;
import com.info121.coach.utils.PrefDB;
import com.info121.coach.utils.Util;

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
                mProgressBar.setVisibility(View.GONE);

                // Add to Appication Varialbles
                App.userName = mUserName.getText().toString();
                App.deviceID = Util.getDeviceID(getApplicationContext());
                App.authToken = response.body().getToken();
                App.timerDelay = 1000;

                // instantiate wiht new Token
                //RestClient.Dismiss();

                prefDB.putString(App.CONST_USER_NAME, App.userName);
                prefDB.putString(App.CONST_DEVICE_ID, App.deviceID);
                prefDB.putLong(App.CONST_TIMER_DELAY, App.timerDelay);

                if (mRemember.isChecked())
                    prefDB.putBoolean(App.CONST_REMEMBER_ME, true);
                else
                    prefDB.putBoolean(App.CONST_REMEMBER_ME, false);

                // login successful
                startActivity(new Intent(LoginActivity.this, JobOverviewActivity.class));
            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                mUserName.setError("Wrong user name");
                mUserName.requestFocus();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
