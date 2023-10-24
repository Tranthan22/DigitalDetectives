package Device_info;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Update_list {
    @FormUrlEncoded
    @POST("update_list.php")
    Call<Update_list_info> update_list(
            @Field("user") String user1,
            @Field("garden_id") String garden_id,
            @Field("sensor_id") String sensor_id,
            @Field("motor_id") String motor_id

    );
}
