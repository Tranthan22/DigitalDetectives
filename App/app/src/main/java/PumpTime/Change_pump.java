package PumpTime;

import Mode.Mannual_control;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Change_pump {
    @FormUrlEncoded
    @POST("update_motor.php")
    Call<Mannual_control> change_pump(
            @Field("user")String user2,
            @Field("motor_id")String node_id1,
            @Field("pump_1")String bump_1,
            @Field("pump_2") String bump_2,
            @Field("pump_3") String bump_3
    );
}
