package com.example.finalmobile2025.api;

import com.example.finalmobile2025.models.PoolingResponse;
import com.example.finalmobile2025.models.TestDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    
    @GET("test/{joinCode}")
    Call<TestDetailsResponse> getTestDetails(@Path("joinCode") String joinCode);
    
    @GET("test/{joinCode}/pooling")
    Call<PoolingResponse> getTestPooling(@Path("joinCode") String joinCode);
}
