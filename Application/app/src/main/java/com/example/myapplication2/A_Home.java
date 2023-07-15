package com.example.myapplication2;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class A_Home extends Fragment {
    private Handler handler = new Handler();
    private Runnable runnable;
    private Change_pump api_change_pump;
    private Change_mode api_change_mode;
    private User_node_name api_get_node_name;
    private User_node_info api_get_node_info;
    private Switch switch10,switch1, switch2,switch3;
    private String BaseUrl="https://digitaldectectives.azdigi.blog/Users/";
    Button imageView,button1, button2, button3 ;

    int SELECT_IMAGE_CODE =1;
    Dialog dialog;
    private LinearLayout linearLayout;
    TextView welcome_user, battery;
    String selectedItem = "", selectedIcon, user_namen, result;
    private String[] items={"Node 1 ", "Node 2 ", "Node 3"};
    private String[] node_info = {"yolo","123"};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a__home, container, false);



        Home home = (Home) getActivity();
        // tim cac phim chuc nang
        battery =(TextView) view.findViewById(R.id.battery);
        welcome_user = (TextView) view.findViewById(R.id.welcome_user);


        switch1 = (Switch) view.findViewById(R.id.switch_1);
        switch2 = (Switch) view.findViewById(R.id.switch_2);
        switch3 = (Switch) view.findViewById(R.id.switch_3);
        switch10 = (Switch) view.findViewById(R.id.switch47);

        imageView = (Button) view.findViewById(R.id.imageView1);
        button1 = (Button) view.findViewById(R.id.button1);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button) view.findViewById(R.id.button3);

        linearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_enabled) ;

        Spinner spinner = view.findViewById(R.id.spinner);


        //Luu user_name tư dang nhap hoac signup vao
        //De hien thi o Home o Setting, va de post len lay du lieu vuon ve
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String usernameLogin = sharedPreferences.getString("usernameLogin", "");
        String usernameSignup = sharedPreferences.getString("usernameSignup", "");

        if (usernameSignup.isEmpty()) {
            user_namen = usernameLogin;
            welcome_user.setText("Welcome " + user_namen + " !   ");

        }
        if (usernameLogin.isEmpty()){
            user_namen = usernameSignup;
            welcome_user.setText("Welcome " + user_namen + " !   ");
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);

        editor.apply();
        // Lay du lieu ten cac node tu database ve
        // Khoi tao spinner chua ten cac node

        getNodename(new OnGetNodeNameListener() {
            @Override
            public void onGetNodeNameSuccess(String[] items) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text, items);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onGetNodeNameError() {
                String[] yo = {"No data"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text, yo);
                spinner.setAdapter(adapter);
            }
        });

        // Spinner chua ten cac vuon
        // Khi chon vuon thi se hien thi thong so cua tung vuon
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = items[i];
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("garden",selectedItem);
                String garden = sharedPreferences.getString("garden","");
                editor.putString("bump_1"+garden,"0");
                editor.putString("bump_2"+garden,"0");
                editor.putString("bump_3"+garden,"0");

                editor.apply();
                getNodeInfo(selectedItem, new OnGetNodeInfoListener() {
                    @Override
                    public void onGetNodeInfoSuccess(String[] node_info) {
                        Node_info nodeInfo = new Node_info(node_info);

                        battery.setText("Battery "+ nodeInfo.getBattery()+"%");
                        button1.setText("Air Humidity\n\n\n\n"+nodeInfo.getNode_air()+"%");
                        button2.setText("PH Sensor\n\n\n\n"+nodeInfo.getNode_PH());
                        button3.setText("Soil Moisture\n\n\n\n"+nodeInfo.getNode_soil()+"%");

                        editor.apply();

                        if (nodeInfo.getNode_auto().equals("1")){
                            switch10.setChecked(false);
                            linearLayout.setEnabled(false);
                            if (nodeInfo.getBump_1().equals("0")) {
                                switch1.setChecked(false);
                                editor.putString("bump_1"+garden,"0");
                                editor.apply();
                            }
                            else {
                                switch1.setChecked(true);
                                editor.putString("bump_1"+garden,"1");
                                editor.apply();
                            }

                            if (nodeInfo.getBump_2().equals("0")) {
                                switch2.setChecked(false);
                                editor.putString("bump_2"+garden,"0");
                                editor.apply();
                            }
                            else {
                                switch2.setChecked(true);
                                editor.putString("bump_2"+garden,"1");
                                editor.apply();
                            }

                            if (nodeInfo.getBump_3().equals("0")) {
                                switch3.setChecked(false);
                                editor.putString("bump_3"+garden,"0");
                                editor.apply();
                            }
                            else {
                                switch3.setChecked(true);
                                editor.putString("bump_3"+garden,"1");
                                editor.apply();
                            }

                            switch1.setEnabled(false);
                            switch2.setEnabled(false);
                            switch3.setEnabled(false);
                            linearLayout.setBackgroundResource(R.drawable.gray);
                        }else {
                            switch10.setChecked(true);
                            linearLayout.setEnabled(true);
                            if (nodeInfo.getBump_1().equals("0")) {
                                switch1.setChecked(false);
                                editor.putString("bump_1"+garden,"0");
                                editor.apply();
                            }
                            else {
                                switch1.setChecked(true);
                                editor.putString("bump_1"+garden,"1");
                                editor.apply();
                            }

                            if (nodeInfo.getBump_2().equals("0")) {
                                switch2.setChecked(false);
                                editor.putString("bump_2"+garden,"0");
                                editor.apply();
                            }
                            else {
                                switch2.setChecked(true);
                                editor.putString("bump_2"+garden,"1");
                                editor.apply();
                            }

                            if (nodeInfo.getBump_3().equals("0")) {
                                switch3.setChecked(false);
                                editor.putString("bump_3"+garden,"0");
                                editor.apply();
                            }
                            else {
                                switch3.setChecked(true);
                                editor.putString("bump_3"+garden,"1");
                                editor.apply();
                            }


                            switch1.setEnabled(true);
                            switch2.setEnabled(true);
                            switch3.setEnabled(true);
                            linearLayout.setBackgroundResource(R.drawable.button);
                        }
                        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();

                        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked){
                                    String bump_1 = "1";

                                    Retrofit retrofit=new Retrofit.Builder()
                                            .baseUrl("https://digitaldectectives.azdigi.blog/main/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    api_change_pump = retrofit.create(Change_pump.class);
                                    String garden1 = sharedPreferences2.getString("garden","");
                                    editor2.putString("bump_1"+garden1,bump_1);
                                    editor2.apply();

                                    String bump_1_get = sharedPreferences2.getString("bump_1"+garden1,"");
                                    String bump_2_get = sharedPreferences2.getString("bump_2"+garden1,"");
                                    String bump_3_get = sharedPreferences2.getString("bump_3"+garden1,"");


                                    Call<Mannual_control> change_pump1 = api_change_pump.change_pump(user_namen,garden1,bump_1_get,bump_2_get,bump_3_get);
                                    change_pump1.enqueue(new Callback<Mannual_control>() {
                                        @Override
                                        public void onResponse(Call<Mannual_control> call, Response<Mannual_control> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<Mannual_control> call, Throwable t) {

                                        }
                                    });
                                }
                                else{
                                    String bump_1 = "0";

                                    Retrofit retrofit=new Retrofit.Builder()
                                            .baseUrl("https://digitaldectectives.azdigi.blog/main/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    api_change_pump = retrofit.create(Change_pump.class);
                                    String garden1 = sharedPreferences2.getString("garden","");
                                    editor2.putString("bump_1"+garden1,bump_1);
                                    editor2.apply();
                                    String bump_1_get = sharedPreferences2.getString("bump_1"+garden1,"");
                                    String bump_2_get = sharedPreferences2.getString("bump_2"+garden1,"");
                                    String bump_3_get = sharedPreferences2.getString("bump_3"+garden1,"");

                                    Call<Mannual_control> change_pump1 = api_change_pump.change_pump(user_namen,garden1,bump_1_get,bump_2_get,bump_3_get);
                                    change_pump1.enqueue(new Callback<Mannual_control>() {
                                        @Override
                                        public void onResponse(Call<Mannual_control> call, Response<Mannual_control> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<Mannual_control> call, Throwable t) {

                                        }
                                    });
                                }
                            }
                        });
                        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked){
                                    String bump_2 = "1";

                                    Retrofit retrofit=new Retrofit.Builder()
                                            .baseUrl("https://digitaldectectives.azdigi.blog/main/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    api_change_pump = retrofit.create(Change_pump.class);
                                    String garden1 = sharedPreferences2.getString("garden","");
                                    editor2.putString("bump_2"+garden1,bump_2);
                                    editor2.apply();
                                    String bump_1_get = sharedPreferences2.getString("bump_1"+garden1,"");
                                    String bump_2_get = sharedPreferences2.getString("bump_2"+garden1,"");
                                    String bump_3_get = sharedPreferences2.getString("bump_3"+garden1,"");

                                    Call<Mannual_control> change_pump1 = api_change_pump.change_pump(user_namen,garden1,bump_1_get,bump_2_get,bump_3_get);
                                    change_pump1.enqueue(new Callback<Mannual_control>() {
                                        @Override
                                        public void onResponse(Call<Mannual_control> call, Response<Mannual_control> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<Mannual_control> call, Throwable t) {

                                        }
                                    });
                                }
                                else{
                                    String bump_2 = "0";

                                    Retrofit retrofit=new Retrofit.Builder()
                                            .baseUrl("https://digitaldectectives.azdigi.blog/main/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    api_change_pump = retrofit.create(Change_pump.class);
                                    String garden1 = sharedPreferences.getString("garden","");
                                    editor2.putString("bump_2"+garden1,bump_2);
                                    editor2.apply();
                                    String bump_1_get = sharedPreferences.getString("bump_1"+garden1,"");
                                    String bump_2_get = sharedPreferences.getString("bump_2"+garden1,"");
                                    String bump_3_get = sharedPreferences.getString("bump_3"+garden1,"");

                                    Call<Mannual_control> change_pump1 = api_change_pump.change_pump(user_namen,garden1,bump_1_get,bump_2_get,bump_3_get);
                                    change_pump1.enqueue(new Callback<Mannual_control>() {
                                        @Override
                                        public void onResponse(Call<Mannual_control> call, Response<Mannual_control> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<Mannual_control> call, Throwable t) {

                                        }
                                    });
                                }
                            }
                        });
                        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked){
                                    String bump_3 = "1";

                                    Retrofit retrofit=new Retrofit.Builder()
                                            .baseUrl("https://digitaldectectives.azdigi.blog/main/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    api_change_pump = retrofit.create(Change_pump.class);
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    String garden1 = sharedPreferences.getString("garden","");
                                    editor2.putString("bump_3"+garden1,bump_3);
                                    editor2.apply();
                                    String bump_1_get = sharedPreferences.getString("bump_1"+garden1,"");
                                    String bump_2_get = sharedPreferences.getString("bump_2"+garden1,"");
                                    String bump_3_get = sharedPreferences.getString("bump_3"+garden1,"");

                                    Call<Mannual_control> change_pump1 = api_change_pump.change_pump(user_namen,garden1,bump_1_get,bump_2_get,bump_3_get);
                                    change_pump1.enqueue(new Callback<Mannual_control>() {
                                        @Override
                                        public void onResponse(Call<Mannual_control> call, Response<Mannual_control> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<Mannual_control> call, Throwable t) {

                                        }
                                    });
                                }
                                else{
                                    String bump_3 = "0";

                                    Retrofit retrofit=new Retrofit.Builder()
                                            .baseUrl("https://digitaldectectives.azdigi.blog/main/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    api_change_pump = retrofit.create(Change_pump.class);
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    String garden1 = sharedPreferences.getString("garden","");
                                    editor2.putString("bump_3"+garden1,bump_3);
                                    editor2.apply();
                                    String bump_1_get = sharedPreferences.getString("bump_1"+garden1,"");
                                    String bump_2_get = sharedPreferences.getString("bump_2"+garden1,"");
                                    String bump_3_get = sharedPreferences.getString("bump_3"+garden1,"");

                                    Call<Mannual_control> change_pump1 = api_change_pump.change_pump(user_namen,garden1,bump_1_get,bump_2_get,bump_3_get);
                                    change_pump1.enqueue(new Callback<Mannual_control>() {
                                        @Override
                                        public void onResponse(Call<Mannual_control> call, Response<Mannual_control> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<Mannual_control> call, Throwable t) {

                                        }
                                    });
                                }
                            }
                        });
                    }
                    @Override
                    public void onGetNodeInfoError() {
                        Toast.makeText(getContext(), "Lỗi kết nối " , Toast.LENGTH_SHORT).show();
                    }
                });

                switch10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (!b){
                            String mode = "1";
                            Retrofit retrofit=new Retrofit.Builder()
                                    .baseUrl("https://digitaldectectives.azdigi.blog/main/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            api_change_mode = retrofit.create(Change_mode.class);

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String garden1 = sharedPreferences.getString("garden","");

                            Call<Mode> change_mode = api_change_mode.change_mode(user_namen,garden1,mode);
                            change_mode.enqueue(new Callback<Mode>() {
                                @Override
                                public void onResponse(Call<Mode> call, Response<Mode> response) {

                                }

                                @Override
                                public void onFailure(Call<Mode> call, Throwable t) {

                                }
                            });
                            linearLayout.setEnabled(false);
                            switch1.setEnabled(false);
                            switch2.setEnabled(false);
                            switch3.setEnabled(false);
                            linearLayout.setBackgroundResource(R.drawable.gray);


                        }else {
                            String mode = "0";

                            Retrofit retrofit=new Retrofit.Builder()
                                    .baseUrl("https://digitaldectectives.azdigi.blog/main/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            api_change_mode = retrofit.create(Change_mode.class);
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String garden1 = sharedPreferences.getString("garden","");


                            Call<Mode> change_mode = api_change_mode.change_mode(user_namen,garden1,mode);
                            change_mode.enqueue(new Callback<Mode>() {
                                @Override
                                public void onResponse(Call<Mode> call, Response<Mode> response) {

                                }

                                @Override
                                public void onFailure(Call<Mode> call, Throwable t) {

                                }
                            });
                            linearLayout.setEnabled(true);
                            switch1.setEnabled(true);
                            switch2.setEnabled(true);
                            switch3.setEnabled(true);

                            linearLayout.setBackgroundResource(R.drawable.button);

                        }
                    }

                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_dialog);
                Button icon1 = (Button) dialog.findViewById(R.id.icon1);
                Button icon2 = (Button) dialog.findViewById(R.id.icon2);
                Button icon3 = (Button) dialog.findViewById(R.id.icon3);
                Button icon4 = (Button) dialog.findViewById(R.id.icon4);
                icon1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageView.setBackgroundResource(R.drawable.ic_home);
                        dialog.dismiss();
                        selectedIcon = "icon1";
                    }
                });
                icon2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageView.setBackgroundResource(R.drawable.ic_dashboard);
                        dialog.dismiss();
                        selectedIcon = "icon2";
                    }
                });
                icon3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageView.setBackgroundResource(R.drawable.ic_notification);
                        dialog.dismiss();
                        selectedIcon = "icon3";
                    }
                });
                icon4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageView.setBackgroundResource(R.drawable.ic_settings);
                        dialog.dismiss();
                        selectedIcon = "icon4";
                    }
                });
                dialog.show();
            }
        });
        String baseUrl = "https://digitaldectectives.azdigi.blog/graph/";
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.webview_info, null);
                builder.setView(dialogView);

                WebView webView = (WebView) dialogView.findViewById(R.id.webview5);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // Handle the URL here to prevent it from loading in the WebView
                        return true;
                    }
                });
                webView.getSettings().setJavaScriptEnabled(true);
                SharedPreferences sharedPreferences4 = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String garden1 = sharedPreferences4.getString("garden","");

                webView.loadUrl(baseUrl+"nodeAir.php?user="+user_namen+"&node_id="+garden1);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        editor.putString("selectedIcon", selectedIcon);
        editor.apply();

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.sendNotification("Hello","haha");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.webview_info, null);
                builder.setView(dialogView);

                WebView webView = (WebView) dialogView.findViewById(R.id.webview5);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // Handle the URL here to prevent it from loading in the WebView
                        return true;
                    }
                });
                webView.getSettings().setJavaScriptEnabled(true);
                SharedPreferences sharedPreferences4 = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String garden1 = sharedPreferences4.getString("garden","");
                webView.loadUrl(baseUrl+"nodePH.php?user="+user_namen+"&node_id="+garden1);

                AlertDialog dialog = builder.create();
                dialog.show();

            }

        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.webview_info, null);
                builder.setView(dialogView);

                WebView webView = (WebView) dialogView.findViewById(R.id.webview5);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // Handle the URL here to prevent it from loading in the WebView
                        return true;
                    }
                });
                webView.getSettings().setJavaScriptEnabled(true);
                SharedPreferences sharedPreferences4 = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String garden1 = sharedPreferences4.getString("garden","");
                webView.loadUrl(baseUrl+"nodeSoil.php?user="+user_namen+"&node_id="+garden1);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return view;
    }
    //Ham timer, moi dang test la gui thong bao

    //Ham lay ten Node
    private void getNodename(OnGetNodeNameListener listener){

        Retrofit retrofit_getInfo = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api_get_node_name = retrofit_getInfo.create(User_node_name.class);

        Call<User_node> callyo = api_get_node_name.getNodes(user_namen);

        callyo.enqueue(new Callback<User_node>() {
            @Override
            public void onResponse(Call<User_node> call, Response<User_node> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response.body());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray tienArray = jsonObject.getJSONArray("tien");
                    items = new String[tienArray.length()];
                    for (int i = 0; i < tienArray.length(); i++) {
                        items[i] = tienArray.getString(i);
                    }
                    if (listener != null) {
                        listener.onGetNodeNameSuccess(items);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getContext(), "Loi may chu",Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<User_node> call, Throwable t) {
                if (listener != null) {
                    listener.onGetNodeNameError();
                }
            }
        });

    }

    //Ham truyen vao ten Node va tra thong tin node ve
    private void getNodeInfo(String node_id,OnGetNodeInfoListener listenerg){
        Retrofit retrofit_getInfo = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api_get_node_info = retrofit_getInfo.create(User_node_info.class);

        Call<User_node_info_1> callb = api_get_node_info.getNodes(user_namen,node_id);

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