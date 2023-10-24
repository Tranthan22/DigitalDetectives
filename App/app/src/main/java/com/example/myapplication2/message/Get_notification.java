package com.example.myapplication2.message;

import com.example.myapplication2.message.Message_info;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Get_notification {
    @FormUrlEncoded
    @POST("messHistory.php")
    Call<Message_info> getMessageinfo(
            @Field("user") String usern
    );

}
