package com.example.myapplication2;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Change_mode {
    @FormUrlEncoded
    @POST("change_mode.php")
    Call<Mode> change_mode(
            @Field("user")String user1,
            @Field("node_id")String node_id,
            @Field("node_auto")String node_auto


    );

}
