package User_info;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface User_node_info {
    @FormUrlEncoded
    @POST("getGarden.php")
    Call<User_node_info_1> getNodes(
            @Field("user") String user_name,
            @Field("garden") String node_id
    );
}
