package com.sustentate.app.api;

import android.content.Context;
import android.net.Uri;

import com.sustentate.app.utils.Constants;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Created by mzorilla on 11/4/17.
 */

public class RetroUpload {

    private Retrofit postImage() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void uploadImage(final ResultListener<String> resultListener, File file) {
        resultListener.loading();

        SustentateAPI sustentateAPI = postImage().create(SustentateAPI.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("sustentable", "" + file, requestFile);

        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, "sustentable");

        Call<ResponseBody> call = sustentateAPI.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                resultListener.finish(response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                resultListener.error(t);
            }
        });
    }
}
