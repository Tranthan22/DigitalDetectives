package com.example.myapplication2;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Set_pump_time {
    @FormUrlEncoded
    @POST("setBumpTime.php")
    Call<PumpTime> set_pump_time(
            @Field("user")String user1,
            @Field("node_id")String node_id,
            @Field("bumpTime")String pump_time
    );
}
