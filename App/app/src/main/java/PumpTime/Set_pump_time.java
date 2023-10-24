package PumpTime;

import Fragment.PumpTime;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Set_pump_time {
    @FormUrlEncoded
    @POST("param_update.php")
    Call<PumpTime> paramUpdate(
            @Field("user")String user1,
            @Field("garden")String node_id,
            @Field("pumpTime")String pump_time,
            @Field("dailyPump")String daily_pump,
            @Field("airWarning")String air_warning,
            @Field("soilWarning")String soil_warning,
            @Field("tempWarning")String temp_warning,
            @Field("batteryWarning")String battery_warning
    );
}
