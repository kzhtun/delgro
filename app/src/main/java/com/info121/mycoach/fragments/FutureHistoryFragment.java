package com.info121.mycoach.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.info121.mycoach.App;
import com.info121.mycoach.R;
import com.info121.mycoach.adapters.JobsAdapter;
import com.info121.mycoach.api.RestClient;
import com.info121.mycoach.models.Job;
import com.info121.mycoach.models.JobRes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FutureHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FutureHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FutureHistoryFragment extends Fragment {

    List<Job> mJobList;

    @BindView(R.id.no_data)
    TextView mNoData;

    @BindView(R.id.rv_jobs)
    RecyclerView mRecyclerView;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout mSwipeLayout;


    @BindView(R.id.passenger)
    EditText mPassenger;

    @BindView(R.id.date)
    EditText mDate;

    @BindView(R.id.sort_layout)
    LinearLayout mSortLayout;

    Context mContext = getActivity();

    String mCurrentTab = "";
    Calendar myCalendar;
    String sort = "0";


    public static FutureHistoryFragment newInstance(String param1) {
        FutureHistoryFragment fragment = new FutureHistoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        fragment.mCurrentTab = param1;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @OnCheckedChanged({R.id.sort_asc, R.id.sort_desc})
    public void onRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.sort_asc:
                    sort = "0";
                    break;
                case R.id.sort_desc:
                    sort = "1";
                    break;
            }
        }
    }

    @OnFocusChange(R.id.date)
    public void dateOnFocusChange(boolean focused) {
        if (focused) showDateDialog();
    }

    @OnClick(R.id.date)
    public void dateOnClick() {
        showDateDialog();
    }

    private void showDateDialog() {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd MMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                mDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        new DatePickerDialog(getContext(), dateListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_future_history, container, false);

        ButterKnife.bind(this, view);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchOnClick();
            }
        });

        myCalendar = Calendar.getInstance();


        if(mCurrentTab.equalsIgnoreCase("HISTORY"))
            mSortLayout.setVisibility(View.INVISIBLE);
        else
            mSortLayout.setVisibility(View.VISIBLE);


        // Inflate the layout for this fragment
        return view;
    }

    @OnClick(R.id.search)
    public void searchOnClick() {
        switch (mCurrentTab) {
            case "FUTURE": {
                getFutureJobs();
            }
            break;
            case "HISTORY": {
                getHistoryJobs();
            }
            break;
        }
    }

    private void getFutureJobs() {
        String customer = " ";

        if(mPassenger.getText().length() > 0)
            customer = mPassenger.getText().toString();

        Call<JobRes> call = RestClient.COACH().getApiService().GetFutureJobs(mDate.getText().toString(),
                customer,
                sort
        );

        call.enqueue(new Callback<JobRes>() {
            @Override
            public void onResponse(Call<JobRes> call, Response<JobRes> response) {
                mSwipeLayout.setRefreshing(false);
                mJobList = (List<Job>) response.body().getJobs();

                App.jobList = mJobList;

                if (mJobList.size() > 0)
                    mNoData.setVisibility(View.GONE);
                else
                    mNoData.setVisibility(View.VISIBLE);

                // set to recyclerview
                mRecyclerView.setHasFixedSize(false);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setAdapter(new JobsAdapter(mContext, mJobList));
                mRecyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<JobRes> call, Throwable t) {

            }
        });


    }

    private void getHistoryJobs() {
        String customer = " ";

        if(mPassenger.getText().length() > 0)
            customer = mPassenger.getText().toString();

        Call<JobRes> call = RestClient.COACH().getApiService().GetHistoryJobs(mDate.getText().toString(),
                customer
        );

        call.enqueue(new Callback<JobRes>() {
            @Override
            public void onResponse(Call<JobRes> call, Response<JobRes> response) {
                mSwipeLayout.setRefreshing(false);
                mJobList = (List<Job>) response.body().getJobs();

                App.jobList = mJobList;

                if (mJobList.size() > 0)
                    mNoData.setVisibility(View.GONE);
                else
                    mNoData.setVisibility(View.VISIBLE);

                // set to recyclerview
                mRecyclerView.setHasFixedSize(false);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setAdapter(new JobsAdapter(mContext, mJobList));
                mRecyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<JobRes> call, Throwable t) {

            }
        });


    }

    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
