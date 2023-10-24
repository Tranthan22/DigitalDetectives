package MainActivity;

import MainActivity.Token;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Token_update {
    @FormUrlEncoded
    @POST("tokenUpdate.php")
    Call<Token> token_update(
            @Field("user")String user1,
            @Field("deviceToken")String token
    );
}
