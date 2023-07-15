package com.example.myapplication2;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface User_node_info {
    @FormUrlEncoded
    @POST("get_node_info.php")
    Call<User_node_info_1> getNodes(
            @Field("user_nameg") String user_name,
            @Field("node_idg") String node_id
    );
}
