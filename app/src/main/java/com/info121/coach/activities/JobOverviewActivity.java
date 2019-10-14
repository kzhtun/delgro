package com.info121.coach.activities;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.info121.coach.AbstractActivity;
import com.info121.coach.App;
import com.info121.coach.R;
import com.info121.coach.adapters.OverviewPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

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


//        mTabLayout.getTabAt(0).setCustomView(R.layout.tab_header);
//        TextView t = mTabLayout.findViewById(R.id.title);
//        t.setText("TODAY");
//
//
//        mTabLayout.getTabAt(1).setCustomView(R.layout.tab_header);
//        TextView t1 = mTabLayout.findViewById(R.id.title);
//        t1.setText("TOMORROW");

//        mTabLayout.getTabAt(2).setCustomView(R.layout.tab_header);
//         t = mTabLayout.findViewById(R.id.title);
//        t.setText("FUTURE");
//
//        mTabLayout.getTabAt(3).setCustomView(R.layout.tab_header);
//         t = mTabLayout.findViewById(R.id.title);
//        t.setText("HISTORY");


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
