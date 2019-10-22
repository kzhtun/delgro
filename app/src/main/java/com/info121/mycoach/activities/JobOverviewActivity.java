package com.info121.mycoach.activities;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.info121.mycoach.AbstractActivity;
import com.info121.mycoach.App;
import com.info121.mycoach.R;
import com.info121.mycoach.adapters.OverviewPageAdapter;
import com.info121.mycoach.api.RestClient;
import com.info121.mycoach.models.JobCount;
import com.info121.mycoach.models.JobRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobOverviewActivity extends AbstractActivity {

    Context mContext = JobOverviewActivity.this;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_overview);

        ButterKnife.bind(this);


        // set toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Welcome " + App.userName);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        TabLayout.Tab tab = mTabLayout.getTabAt(0);
//        TextView textView = new TextView(mContext);
//        textView.setText("AAAA");
//        tab.setCustomView(textView);


        // set view pager
        OverviewPageAdapter pageAdapter = new OverviewPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        callJobsCount(false);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //App.test = i + "";

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    private void callJobsCount(final Boolean update) {
        Call<JobRes> call = RestClient.COACH().getApiService().GetJobsCount();

        call.enqueue(new Callback<JobRes>() {
            @Override
            public void onResponse(Call<JobRes> call, Response<JobRes> response) {
                if (update)
                    updateTabs(response.body().getJobcountlist());
                else
                    initializeTabs(response.body().getJobcountlist());
            }

            @Override
            public void onFailure(Call<JobRes> call, Throwable t) {

            }
        });
    }

    private void initializeTabs(List<JobCount> jobCountList) {
        TabLayout.Tab tabitem;
        View v;
        TextView header, badge;

        JobCount jobCount = jobCountList.get(0);

        //  TabLayout.Tab tabitem = mTabLayout.newTab();

        tabitem = mTabLayout.getTabAt(0);
        v = View.inflate(mContext, R.layout.tab_header, null);
        header = v.findViewById(R.id.title);
        badge = v.findViewById(R.id.job_count);
        header.setText("TODAY");
        badge.setText(jobCount.getTodayjobcount());
        tabitem.setCustomView(v);

        tabitem = mTabLayout.getTabAt(1);
        v = View.inflate(mContext, R.layout.tab_header, null);
        header = v.findViewById(R.id.title);
        badge = v.findViewById(R.id.job_count);
        header.setText("TMR");
        badge.setText(jobCount.getTomorrowjobcount());
        tabitem.setCustomView(v);


        tabitem = mTabLayout.getTabAt(2);
        v = View.inflate(mContext, R.layout.tab_header, null);
        header = v.findViewById(R.id.title);
        badge = v.findViewById(R.id.job_count);
        header.setText("FUTURE");
        badge.setText(jobCount.getFuturejobcount());
        tabitem.setCustomView(v);


        tabitem = mTabLayout.getTabAt(3);
        v = View.inflate(mContext, R.layout.tab_header, null);
        header = v.findViewById(R.id.title);
        badge = v.findViewById(R.id.job_count);
        header.setText("HISTORY");
        badge.setText("0");
        tabitem.setCustomView(v);
    }

    private void updateTabs(List<JobCount> jobCountList) {
        TabLayout.Tab tabitem;
        View v;
        TextView header, badge;

        JobCount jobCount = jobCountList.get(0);

        tabitem = mTabLayout.getTabAt(0);
        badge = tabitem.getCustomView().findViewById(R.id.job_count);
        badge.setText(jobCount.getTodayjobcount());

        tabitem = mTabLayout.getTabAt(1);
        badge = tabitem.getCustomView().findViewById(R.id.job_count);
        badge.setText(jobCount.getTomorrowjobcount());

        tabitem = mTabLayout.getTabAt(2);
        badge = tabitem.getCustomView().findViewById(R.id.job_count);
        badge.setText(jobCount.getFuturejobcount());

        tabitem = mTabLayout.getTabAt(3);
        badge = tabitem.getCustomView().findViewById(R.id.job_count);
        badge.setText("0");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle Action bar item clicks here. The Action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Subscribe(sticky = true)
    public void onEvent(String event) {
        EventBus.getDefault().removeStickyEvent("UPDATE_JOB_COUNT");
        Toast.makeText(mContext, "Update Job List", Toast.LENGTH_SHORT).show();
        callJobsCount(true);
    }
}
