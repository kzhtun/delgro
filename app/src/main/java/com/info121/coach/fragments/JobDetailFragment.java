package com.info121.coach.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.info121.coach.AbstractFragment;
import com.info121.coach.R;
import com.info121.coach.models.Job;

import butterknife.BindView;
import butterknife.ButterKnife;


public class JobDetailFragment extends Fragment {
    Job job;

    @BindView(R.id.job_no)
    TextView mJobNo;

    @BindView(R.id.job_type)
    TextView mJobType;

    @BindView(R.id.job_status)
    TextView mJobStatus;

    @BindView(R.id.date)
    TextView mDate;

    @BindView(R.id.time)
    TextView mTime;

    @BindView(R.id.passenger)
    TextView mPassenger;

    @BindView(R.id.mobile)
    TextView mMobile;


    @BindView(R.id.flight_no)
    TextView mFlightNo;

    @BindView(R.id.eta)
    TextView mETA;

    @BindView(R.id.pickup)
    TextView mPickup;

    @BindView(R.id.dropoff)
    TextView mDropOff;

    @BindView(R.id.remarks)
    TextView mRemarks;

    public JobDetailFragment() {
        // Required empty public constructor
    }


    public static JobDetailFragment newInstance(Job job) {
        JobDetailFragment fragment = new JobDetailFragment();
        Bundle args = new Bundle();

        fragment.job = job;
        fragment.setArguments(args);



        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_job_detail, container, false);

        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayJobDetail();
    }

    private void displayJobDetail(){
        mJobNo.setText(job.getBookNo());
        mJobType.setText(job.getJobType());
        mJobStatus.setText(job.getJobStatus());
        mDate.setText(job.getUsageDate());
        mTime.setText(job.getPickUpTime());
        mPassenger.setText(job.getCustomer());
        mMobile.setText(job.getCustomerTel());
        mFlightNo.setText("");
        mETA.setText("");
        mPickup.setText(job.getPickUp());
        mDropOff.setText(job.getDestination());
        mRemarks.setText("");
    }



    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
