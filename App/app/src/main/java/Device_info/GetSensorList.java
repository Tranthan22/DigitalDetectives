package Device_info;

import User_info.User_node;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetSensorList {

    @FormUrlEncoded
    @POST("disconnected_device_list.php")
    Call<Sensor_list> getSensor(
            @Field("user_nameg") String user_name
    );
}
