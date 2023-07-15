package com.example.myapplication2;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface User_node_name {

    @FormUrlEncoded
    @POST("node_counter.php")
    Call<User_node> getNodes(
            @Field("user_namen") String user_name
    );
}
