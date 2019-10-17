package com.info121.mycoach.api;


import com.info121.mycoach.models.JobRes;
import com.info121.mycoach.models.ObjectRes;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    @GET("validatedriver/{user}")
    Call<ObjectRes> ValidateDriver(@Path("user") String user);

    @GET("getTodayJobsList")
    Call<JobRes> GetTodayJobs();

    @GET("getTomorrowJobsList")
    Call<JobRes> GetTomorrowJobs();

    @GET("getFutureJobsList/{date},{passenger},{sort}")
    Call<JobRes> GetFutureJobs(@Path("date") String date, @Path("passenger") String passenger, @Path("sort") String sort);

    @GET("getHistoryJobsList/{date},{passenger}")
    Call<JobRes> GetHistoryJobs(@Path("date") String date, @Path("passenger") String passenger);

    @GET("updateJobStatus/{jobno},{status},{address}")
    Call<JobRes> UpdateJobStatus(@Path("jobno") String jobno, @Path("status") String status, @Path("address") String address);

    @GET("updateShowConfirmJob/{jobno},{address},{remarks},{status}")
    Call<JobRes> UpdateShowConfirmJob(@Path("jobno") String jobno,  @Path("address") String address,@Path("remarks") String remarks, @Path("status") String status);

    @GET("updateNoShowJob/{jobno},{address},{remarks},{status}")
    Call<JobRes> UpdateNoShowConfirmJob(@Path("jobno") String jobno,  @Path("address") String address,@Path("remarks") String remarks, @Path("status") String status);

    @GET("updateCompleteJob/{jobno},{address},{remarks},{status}")
    Call<JobRes> UpdateCompletJob(@Path("jobno") String jobno,  @Path("address") String address,@Path("remarks") String remarks, @Path("status") String status);

//    //amad,123,android,241jlksfljsaf
//    @GET("updatedriverdetail/{user},{deviceId},{deviceType},{fcm_token}")
//    Call<UpdateDriverDetailRes> updateDriverDetail(@Path("user") String user, @Path("deviceId") String deviceId, @Path("deviceType") String deviceType, @Path("fcm_token") String fcm_token);
//
//    //amad,1.299654,103.855107,0
//    @GET("getdriverlocation/{user},{latitude},{longitude},{status},{address}")
//    Call<UpdateDriverLocationRes> updateDriverLocation(@Path("user") String user, @Path("latitude") String latitude, @Path("longitude") String longitude, @Path("status") int status, @Path("address") String addresss);
//
//
//    @GET("saveshowpic/{user},{job_no},{filename}")
//    Call<SaveShowPicRes> saveShowPic(@Path("user") String user, @Path("job_no") String jobNo, @Path("filename") String fileName);
//
//    @GET("confirmjobreminder/{job_no}")
//    Call<ConfirmJobRes> confirmJob(@Path("job_no") String jobNo);
//
//    @GET("remindmelater/{job_no}")
//    Call<RemindLaterRes> remindLater(@Path("job_no") String jobNo);
//
//    @GET("product")
//    Call<List<product>> getProduct();
//
//    @GET("getversion/AndriodV-{versionCode}")
//    Call<VersionRes> checkVersion(@Path("versionCode") String versionCode);
//
//
//    @GET("savesignature/{jobNo},{fileName}")
//    Call<SaveSignatureRes> savesignature(@Path("jobNo") String jobNo, @Path("fileName") String fileName);
}
