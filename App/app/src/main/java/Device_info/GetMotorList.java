package Device_info;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetMotorList {
    @FormUrlEncoded
    @POST("disconnected_device_list.php")
    Call<Motor_List> getMotor(
            @Field("user_nameg") String user_name
    );
}
