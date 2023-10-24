package MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Check_user {
    @FormUrlEncoded
    @POST("check_user.php")
    Call<Users_check> check_user(
            @Field("user_namel")String usernameLogin,
            @Field("passwordl")String passwordLogin
    );

    @GET("fetch.php")
    Call<List<Users>>fetchData();
}
