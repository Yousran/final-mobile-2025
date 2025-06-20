package com.example.finalmobile2025.api;

import com.example.finalmobile2025.models.PoolingResponse;
import com.example.finalmobile2025.models.TestDetailsResponse;
import com.example.finalmobile2025.models.StartTestRequest;
import com.example.finalmobile2025.models.StartTestResponse;
import com.example.finalmobile2025.models.ParticipantData;
import com.example.finalmobile2025.models.EssayAnswerRequest;
import com.example.finalmobile2025.models.EssayAnswerResponse;
import com.example.finalmobile2025.models.TestResultResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    
    @GET("test/{joinCode}")
    Call<TestDetailsResponse> getTestDetails(@Path("joinCode") String joinCode);
    
    @GET("test/{joinCode}/pooling")
    Call<PoolingResponse> getTestPooling(@Path("joinCode") String joinCode);
    
    @POST("test/{joinCode}/start")
    Call<StartTestResponse> startTest(@Path("joinCode") String joinCode, @Body StartTestRequest request);
    
    @GET("participant/{participantId}")
    Call<ParticipantData> getParticipantData(@Path("participantId") String participantId);
    
    @PATCH("answer/essay")
    Call<EssayAnswerResponse> submitEssayAnswer(@Body EssayAnswerRequest request);
    
    @GET("test/result/{participantId}")
    Call<TestResultResponse> getTestResult(@Path("participantId") String participantId);
}
