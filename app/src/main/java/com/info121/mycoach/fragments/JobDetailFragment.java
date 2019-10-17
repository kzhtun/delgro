package com.info121.mycoach.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.info121.mycoach.App;
import com.info121.mycoach.R;
import com.info121.mycoach.api.RestClient;
import com.info121.mycoach.models.Job;
import com.info121.mycoach.models.JobRes;
import com.info121.mycoach.utils.FtpHelper;

import java.io.File;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.info121.mycoach.utils.FtpHelper.getImageUri;
import static com.info121.mycoach.utils.FtpHelper.getRealPathFromURI;


public class JobDetailFragment extends Fragment {
    Job job;
    private static final int REQUEST_CAMERA_IMAGE = 2001;

    Dialog dialog;

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

    @BindView(R.id.assign_layout)
    LinearLayout mAssignLayout;

    @BindView(R.id.update_layout)
    LinearLayout mUpdateLayout;

    @BindView(R.id.update_status)
    Button mUpdateStatus;

    @BindView(R.id.itinerary)
    Button mItinerary;

    Boolean visible = false;

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

        if (job.getJobStatus().equalsIgnoreCase("JOB ASSIGNED")) {
            mAssignLayout.setVisibility(View.VISIBLE);
            mUpdateLayout.setVisibility(View.GONE);
        } else {
            mAssignLayout.setVisibility(View.GONE);
            mUpdateLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }


    @OnClick(R.id.update_status)
    public void updateStatusOnClick() {

        App.fullAddress = (App.fullAddress.isEmpty()) ? " " : App.fullAddress;

        dialog = new Dialog(getContext());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(R.layout.dialog_update_job);
        dialog.setTitle("Update Job");

        LinearLayout confirm = dialog.findViewById(R.id.confirm);
        LinearLayout otw = dialog.findViewById(R.id.otw);
        LinearLayout onSite = dialog.findViewById(R.id.on_site);
        LinearLayout pob = dialog.findViewById(R.id.pob);
        LinearLayout pns = dialog.findViewById(R.id.pns);
        LinearLayout cnpl = dialog.findViewById(R.id.cnpl);


        switch (job.getJobStatus().toUpperCase()) {
            case "CONFIRM":
                changeUpdateButtonBackground(otw, true);
                break;

            case "ON THE WAY":
                changeUpdateButtonBackground(onSite, true);
                break;

            case "ON SITE":
                changeUpdateButtonBackground(pob, true);
                break;

            case "PASSENGER ON BOARD":
                changeUpdateButtonBackground(cnpl, true);
                break;

//            case "PASSENGER NO SHOW":
//                changeUpdateButtonBackground(cnpl, true);
//                break;

//            case "COMPLETE":
//                changeUpdateButtonBackground(cnpl, true);
//                break;
        }


        otw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJobStatus("On The Way");
                dialog.dismiss();
            }
        });

        onSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJobStatus("On Site");
                dialog.dismiss();
            }
        });

        pob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showPassengerOnBoardDialog();

            }
        });

        pns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NoshowPassengerOnBoardDialog();
            }
        });

        cnpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showCompleteDialog();
            }
        });

        dialog.show();

        //  dialog

    }

    private void showCompleteDialog() {
        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_complete);
        dialog.setTitle("POB ");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView date = dialog.findViewById(R.id.date);
        TextView time = dialog.findViewById(R.id.time);
        TextView location = dialog.findViewById(R.id.location);

        date.setText(job.getUsageDate());
        time.setText(job.getPickUpTime());
        location.setText(job.getLocation());

        Button complete = dialog.findViewById(R.id.complete);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCompletedJob();
            }
        });

        // resize dialog
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (displayRectangle.width() * 0.85f);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void NoshowPassengerOnBoardDialog() {
        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_pob);
        dialog.setTitle("POB ");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.title);
        ImageView addPhoto = dialog.findViewById(R.id.add_photo);
        Button save = dialog.findViewById(R.id.save);

        title.setText("PASSENGER NO SHOW");

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(job.getBookNo() + ".jpg", job.getJobNo());
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdateNoShowPassenger();
            }
        });

        // resize dialog
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (displayRectangle.width() * 0.85f);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showPassengerOnBoardDialog() {
        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_pob);
        dialog.setTitle("POB ");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.title);
        ImageView addPhoto = dialog.findViewById(R.id.add_photo);
        Button save = dialog.findViewById(R.id.save);

        title.setText("PASSENGER ON BOARD");

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(job.getBookNo() + ".jpg", job.getJobNo());
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdateShowPassenger();
            }
        });


        // resize dialog
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (displayRectangle.width() * 0.85f);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void callUpdateShowPassenger() {
        EditText remark = dialog.findViewById(R.id.remarks);
        Call<JobRes> call = RestClient.COACH().getApiService().UpdateShowConfirmJob(
                job.getJobNo(),
                App.fullAddress,
                remark.getText().toString(),
                "Passenger On Board"
        );

        call.enqueue(new Callback<JobRes>() {
            @Override
            public void onResponse(Call<JobRes> call, Response<JobRes> response) {
                Toast.makeText(getContext(), "Passenger On Board Successful", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                displayUpdateStatus("Passenger On Board");
            }

            @Override
            public void onFailure(Call<JobRes> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void callUpdateNoShowPassenger() {
        EditText remark = dialog.findViewById(R.id.remarks);
        Call<JobRes> call = RestClient.COACH().getApiService().UpdateNoShowConfirmJob(
                job.getJobNo(),
                App.fullAddress,
                remark.getText().toString(),
                "Passenger No Show"
        );

        call.enqueue(new Callback<JobRes>() {
            @Override
            public void onResponse(Call<JobRes> call, Response<JobRes> response) {
                Toast.makeText(getContext(), "Passenger No Show Successful", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JobRes> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void callCompletedJob() {
        EditText remark = dialog.findViewById(R.id.remarks);
        Call<JobRes> call = RestClient.COACH().getApiService().UpdateCompletJob(
                job.getJobNo(),
                App.fullAddress,
                remark.getText().toString(),
                "Completed"
        );

        call.enqueue(new Callback<JobRes>() {
            @Override
            public void onResponse(Call<JobRes> call, Response<JobRes> response) {
                Toast.makeText(getContext(), "Job Complete Successful", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JobRes> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


    @OnClick(R.id.phone)
    public void phoneOnClick() {

        if (job.getCustomerTel().length() > 0) {
            String phoneNo = job.getCustomerTel();
            Uri number = Uri.parse("tel:" + phoneNo);
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);

            Toast.makeText(getContext(), "Phone Call .... to  " + phoneNo, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "There is no phone number", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.sms)
    public void smsOnClick() {
        if (job.getCustomerTel().length() > 0) {
            String phoneNo = job.getCustomerTel();
            String msg = "";
            Uri number = Uri.parse("sms:" + phoneNo);
            Intent smsIntent = new Intent(Intent.ACTION_VIEW, number);
            startActivity(smsIntent);

            Toast.makeText(getContext(), "Send SMS .... to  " + phoneNo, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "There is no phone number", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA_IMAGE && resultCode == RESULT_OK) {
            try {

                Bundle extras = data.getExtras();

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                // display passenger photo
                ImageView passenger_photo = dialog.findViewById(R.id.passenger_photo);
                passenger_photo.setImageBitmap(photo);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP

                Uri tempUri = getImageUri(getActivity().getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(getActivity().getApplicationContext(), tempUri));

                InputStream inputStream = getActivity().getContentResolver().openInputStream(tempUri);

                FtpHelper.uploadTask async = new FtpHelper.uploadTask(getContext(), inputStream);
                async.execute(App.FTP_URL, App.FTP_USER, App.FTP_PASSWORD, App.FTP_DIR, job.getBookNo() + ".jpg", job.getJobNo(), "PHOTO");   //Passing arguments to AsyncThread


            } catch (Exception e) {
                Log.e("Camera Error : ", e.getLocalizedMessage().toString());
            }
        }
    }


    public void openCamera(final String fileName, final String jobNo) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra("filename", fileName);
                startActivityForResult(intent, REQUEST_CAMERA_IMAGE);
            }
        });
    }


    private void changeUpdateButtonBackground(LinearLayout layout, boolean selected) {
        final int sdk = android.os.Build.VERSION.SDK_INT;

        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),
                    selected ? R.drawable.rounded_button_orange : R.drawable.rounded_button
            ));
        } else {
            layout.setBackground(ContextCompat.getDrawable(getContext(),
                    selected ? R.drawable.rounded_button_orange : R.drawable.rounded_button));
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {

        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayJobDetail();
    }


    private void displayUpdateStatus(String status) {
        mJobStatus.setText(status);
    }

    private void displayJobDetail() {
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

        mItinerary.setVisibility((job.getFile1().isEmpty()) ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.itinerary)
    public void itineraryOnClick() {

        Uri uri = Uri.parse(App.CONST_PDF_URL +  job.getFile1().toString().trim());

        // create an intent builder
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // Begin customizing
        // set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.colorPrimaryDark));

        // set start and exit animations
        //            intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        //            intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
        //                    android.R.anim.slide_out_right);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // launch the url
        customTabsIntent.launchUrl(getActivity().getBaseContext(), uri);

    }


    @OnClick(R.id.accept)
    public void acceptOnClick() {
        updateJobStatus("Confirm");
    }

    @OnClick(R.id.reject)
    public void rejectOnClick() {
        updateJobStatus("Rejected");
    }

    private void updateJobStatus(final String status) {
        Call<JobRes> call = RestClient.COACH().getApiService().UpdateJobStatus(
                job.getJobNo(),
                status,
                App.fullAddress
        );

        call.enqueue(new Callback<JobRes>() {
            @Override
            public void onResponse(Call<JobRes> call, Response<JobRes> response) {
                Log.e("Update Job Successful", response.toString());
                mAssignLayout.setVisibility(View.GONE);
                mUpdateLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();

                displayUpdateStatus(status);
            }

            @Override
            public void onFailure(Call<JobRes> call, Throwable t) {
                Log.e("Update Job Failed ", t.getMessage());
            }
        });
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
