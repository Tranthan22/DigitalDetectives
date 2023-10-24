package MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Insert_user {
    @FormUrlEncoded
    @POST("insert_user.php")
    Call<Users> insert_Data(
            @Field("user_namet")String user_nameInput,
            @Field("passwordt")String passwordInput,
            @Field("emailt")String emailInput,
            @Field("phonenumbert")String phone_numberInput
    );
    @GET("fetch.php")
    Call<List<Users>>fetchData();
}
