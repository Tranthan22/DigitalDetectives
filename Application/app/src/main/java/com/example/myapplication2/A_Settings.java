package com.example.myapplication2;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class A_Settings extends Fragment {
    Button signout,icon_settings, btn_ok;
    private int x,g;
    private Set_pump_time api_set_pump_time;
    private User_node_info api_get_node_info;
    private String BaseUrl="https://digitaldectectives.azdigi.blog/Users/";
    Dialog dialog_settings;
    SeekBar seekBar_setBumpTime;
    Spinner spinner_setLanguage;
    String username, garden, getPumpTime="";
    TextView username_settings, settings, seekbar_status;
    private String[] node_info = {"yolo","123"};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a__settings, container, false);


        username_settings= view.findViewById(R.id.username_settings);

        signout = view.findViewById(R.id.signout_settings);
        settings = view.findViewById(R.id.settings_settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_settings = new Dialog(getContext());
                dialog_settings.setContentView(R.layout.settings_dialog);


                Window window = dialog_settings.getWindow();
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(15); // Đặt bán kính bo góc
                window.setBackgroundDrawable(gradientDrawable);
                dialog_settings.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);

                seekBar_setBumpTime = (SeekBar) dialog_settings.findViewById(R.id.seekBar_setPumpTime);
                spinner_setLanguage = (Spinner) dialog_settings.findViewById(R.id.spinner_language);
                seekbar_status = (TextView) dialog_settings.findViewById(R.id.textview_seekBar);
                btn_ok = (Button) dialog_settings.findViewById(R.id.btn_settings_ok);

                String[] items = {"Vietnamese", "English"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text_settings, items);
                spinner_setLanguage.setAdapter(adapter);



                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String garden1 = sharedPreferences.getString("garden","");
                String usernameLogin = sharedPreferences.getString("usernameLogin", "");
                String usernameSignup = sharedPreferences.getString("usernameSignup", "");

                if (usernameSignup.isEmpty()) {
                    username = usernameLogin;
                }
                if (usernameLogin.isEmpty()){
                    username = usernameSignup;
                }
                getNodeInfo(garden1, new OnGetNodeInfoListener() {
                    @Override
                    public void onGetNodeInfoSuccess(String[] node_info) {
                        Node_info nodeInfo = new Node_info(node_info);
                        editor.putString("pumpTime"+garden1,nodeInfo.getBumpTime());
                        getPumpTime= sharedPreferences.getString("pumpTime"+garden1,"");
                        if (getPumpTime.isEmpty()){
                            g= 0;
                        }else {
                            g = Integer.valueOf(getPumpTime);
                        }
                        seekbar_status.setText(g+ " minutes");
                        seekBar_setBumpTime.setProgress(g);
                        Toast.makeText(getContext(),getPumpTime,Toast.LENGTH_SHORT).show();
                        editor.apply();
                    }

                    @Override
                    public void onGetNodeInfoError() {
                        Toast.makeText(getContext(), "Lỗi kết nối " , Toast.LENGTH_SHORT).show();
                    }
                });


                seekBar_setBumpTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                        seekbar_status.setText(i+" minutes");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        x = seekBar.getProgress();

                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        editor.putString("pumpTime"+garden1,String.valueOf(x));
                        editor.apply();
                        String pumpTime = sharedPreferences.getString("pumpTime"+garden1,"");



                        Retrofit retrofit=new Retrofit.Builder()
                                .baseUrl("https://digitaldectectives.azdigi.blog/setting/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        api_set_pump_time = retrofit.create(Set_pump_time.class);
                        Call<PumpTime> change_pump_time = api_set_pump_time.set_pump_time(username,garden1, pumpTime);
                        change_pump_time.enqueue(new Callback<PumpTime>() {
                            @Override
                            public void onResponse(Call<PumpTime> call, Response<PumpTime> response) {

                            }

                            @Override
                            public void onFailure(Call<PumpTime> call, Throwable t) {

                            }
                        });


                        dialog_settings.dismiss();
                    }
                });
                dialog_settings.show();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                // Tạo Intent để chuyển đến Activity Welcome_login
                Intent intent = new Intent(getActivity(), Welcome_login.class);

                // Xóa các Activity khác ra khỏi Stack và khởi động Activity Welcome_login
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // Kết thúc Fragment hiện tại
                getActivity().finish();
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String usernameLogin = sharedPreferences.getString("usernameLogin", "");
        String usernameSignup = sharedPreferences.getString("usernameSignup", "");

        if (usernameSignup.isEmpty()) username_settings.setText(usernameLogin);
        if (usernameLogin.isEmpty()) username_settings.setText(usernameSignup);

        return view;

    }
    private void getNodeInfo(String node_id,OnGetNodeInfoListener listenerg){
        Retrofit retrofit_getInfo = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api_get_node_info = retrofit_getInfo.create(User_node_info.class);

        Call<User_node_info_1> callb = api_get_node_info.getNodes(username,node_id);

        callb.enqueue(new Callback<User_node_info_1>() {
            @Override
            public void onResponse(Call<User_node_info_1> call, Response<User_node_info_1> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response.body());

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray gardenArray = jsonObject.getJSONArray("garden");
                    node_info = new String[gardenArray.length()];
                    for (int i = 0; i < gardenArray.length(); i++) {
                        node_info[i] = gardenArray.getString(i);
                    }

                    if (listenerg != null) {
                        listenerg.onGetNodeInfoSuccess(node_info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Loi tra ve du lieu",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User_node_info_1> call, Throwable t) {
                Toast.makeText(getContext(), "Loi ket noi",Toast.LENGTH_LONG).show();

            }
        });

    }

}