package com.sustentate.app.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/*
 * Created by mzorilla on 11/4/17.
 */

public interface SustentateAPI {

    @Multipart
    @POST("subirImagen")
    Call<ResponseBody> upload(@Part("sustentate") RequestBody description, @Part MultipartBody.Part file);
}
