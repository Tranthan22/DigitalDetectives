package Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication2.message.Get_notification;
import com.example.myapplication2.message.Message_info;
import com.example.myapplication2.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class A_Notification extends Fragment {

    private Button get_node_info;
    private String BaseUrl="https://digitaldetectives.top/Message/";

    private String user_namen;
    LinearLayout linearLayout;
    private Get_notification api_get_notification;
    private String[] message_info = {"yolo","123"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.notification, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String usernameLogin = sharedPreferences.getString("usernameLogin", "");
        String usernameSignup = sharedPreferences.getString("usernameSignup", "");
        linearLayout = view.findViewById(R.id.layout_notification);

        if (usernameSignup.isEmpty()) {
            user_namen = usernameLogin;


        }
        if (usernameLogin.isEmpty()){
            user_namen = usernameSignup;

        }

        Retrofit retrofit_getInfoyo = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api_get_notification = retrofit_getInfoyo.create(Get_notification.class);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////dang fix cung day nhe user = tien////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Call<Message_info> calls = api_get_notification.getMessageinfo(user_namen);

        calls.enqueue(new Callback<Message_info>() {
            @Override
            public void onResponse(Call<Message_info> call, Response<Message_info> response) {

                if (response.isSuccessful()) {
                    Message_info messageInfo = response.body();
                    ArrayList<String> notificationList = messageInfo.getNotification();

                    // Chuyển đổi danh sách thành mảng String
                    String[] notificationArray = new String[notificationList.size()];
                    notificationArray = notificationList.toArray(notificationArray);

                    // Xử lý danh sách thông báo
                    if (notificationArray.length > 0) {
                        for (String notification : notificationArray) {
                            // Thực hiện xử lý với từng thông báo
                            String[] parts = notification.split("\\*");
                            String description = parts[0];
                            String dateTime = parts[1];
                                // Tạo một TextView từ layout
                            TextView notification1 = (TextView) inflater.inflate(R.layout.textview_notification, linearLayout, false);
                                // Đặt nội dung cho TextView
                            TextView datetime = (TextView) inflater.inflate(R.layout.textview_datetime,linearLayout,false) ;
                            notification1.setText(description);
                            datetime.setText(dateTime);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.gravity = Gravity.END;
                            datetime.setLayoutParams(layoutParams);

                                // Thêm TextView vào LinearLayout
                            linearLayout.addView(notification1);
                            linearLayout.addView(datetime);

                        }
                    } else {
                        // Không có thông báo nào
                    }

                } else {
                    // Xử lý khi response không thành công
//                    Toast.makeText(getContext(), "Lỗi truy vấn dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Message_info> call, Throwable t) {
                // Xử lý khi request thất bại
//                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_LONG).show();
            }
        });





        return view;
    }

}