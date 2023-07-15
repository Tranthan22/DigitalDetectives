package com.example.myapplication2;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Change_pump {
    @FormUrlEncoded
    @POST("manual_control.php")
    Call<Mannual_control> change_pump(
            @Field("userk")String user2,
            @Field("node_idk")String node_id1,
            @Field("bump_1")String bump_1,
            @Field("bump_2") String bump_2,
            @Field("bump_3") String bump_3
    );
}
