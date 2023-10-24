package NewGarden;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Add_new_garden {
    @FormUrlEncoded
    @POST("addGarden.php")
    Call<NewGarden> add_garden(
            @Field("user") String user1,
            @Field("sensor_id") String sensor_id,
            @Field("motor_id") String motor_id
    );
}
