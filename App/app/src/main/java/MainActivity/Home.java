package MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.messaging.FirebaseMessaging;

import Fragment.A_Fragment_Adapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {


    private static final int NOTIFICATION_ID = 1;
    private String user_namen;
    Token_update api_token_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("hahahehe", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token",token);
                        editor.apply();
                        String yolo = sharedPreferences.getString("token","");
                        String usernameLogin = sharedPreferences.getString("usernameLogin", "");
                        String usernameSignup = sharedPreferences.getString("usernameSignup", "");

                        if (usernameSignup.isEmpty()) {
                            user_namen = usernameLogin;


                        }
                        if (usernameLogin.isEmpty()){
                            user_namen = usernameSignup;

                        }
//                        Toast.makeText(Home.this, user_namen + " home",Toast.LENGTH_SHORT ).show();

                        Retrofit retrofit=new Retrofit.Builder()
                                .baseUrl("https://digitaldetectives.top/App/User/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        api_token_update= retrofit.create(Token_update.class);
//                        Toast.makeText(Home.this, user_namen, Toast.LENGTH_SHORT).show();
                        Call<Token> tokenCall = api_token_update.token_update(user_namen,yolo);
                        tokenCall.enqueue(new Callback<Token>() {
                            @Override
                            public void onResponse(Call<Token> call, Response<Token> response) {

                            }

                            @Override
                            public void onFailure(Call<Token> call, Throwable t) {

                            }
                        });



                        // Log and toast
                        Log.e("hahahehe", "Refreshed token: " + token);

                    }
                });
        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new A_Fragment_Adapter(this));
        TabLayout tabLayout = findViewById(R.id.tablayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Home");
                        tab.setIcon(R.drawable.ic_home);
                        break;

                    case 1:
                        tab.setText("Notifications");
                        tab.setIcon(R.drawable.ic_notification);
                        break;
                    case 2:
                        tab.setText("Settings");
                        tab.setIcon(R.drawable.ic_settings);
                        break;
                }
            }
        }
        );

        tabLayoutMediator.attach();
    }

//    public void sendNotification(String title, String content) {
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
//
//            // Configure the notification channel.
//            notificationChannel.setDescription("Channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationManager.createNotificationChannel(notificationChannel);
//            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
//        }
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//
//        notificationBuilder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.ic_notification)
//                .setTicker("Hearty365")
//                .setContentTitle(title)
//                .setContentText(content)
//                .setContentInfo("Info")
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
//    }
    public void sendNotification(String title, String content, String channelId) {
    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel notificationChannel = new NotificationChannel(channelId, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("Channel description");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
    }

    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);

    notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_notification)
            .setTicker("Hearty365")
            .setContentTitle(title)
            .setContentText(content)
            .setContentInfo("Info")
            .setPriority(NotificationCompat.PRIORITY_HIGH);
    notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


}