package Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication2.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import Device_info.GetMotorList;
import Device_info.GetSensorList;
import Device_info.Motor_List;
import Device_info.OnGetMotorListListener;
import Device_info.OnGetSensorListListener;
import Device_info.Sensor_list;
import MainActivity.Token;
import MainActivity.Token_update;
import MainActivity.Welcome_login;
import NewGarden.Add_new_garden;
import NewGarden.NewGarden;
import PumpTime.Set_pump_time;
import User_info.Node_info;
import User_info.OnGetNodeInfoListener;
import User_info.OnGetNodeNameListener;
import User_info.User_node;
import User_info.User_node_info;
import User_info.User_node_info_1;
import User_info.User_node_name;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class A_Settings extends Fragment {
    Button signout,icon_settings, btn_ok, add_device, confirm_add_device,btn_test,edit_connected_device;
    private int x,g;
    private Set_pump_time api_set_pump_time;
    private static ArrayList<String[]> dataList,gardenList;
    private CustomListAdapter customListAdapter;
    private Switch switch_daily_pump;
    private LinearLayout time_picker;
    Token_update api_token_update;
    private View overlay;
    private int selectedHour = 0;
    private GetSensorList api_get_sensor_list;
    private Set_pump_time api_param_update;

    private Add_new_garden api_new_garden;
    private GetMotorList api_get_motor_list;
    private User_node_name api_get_node_name;
    private String[] items={"Node 1 ", "Node 2 ", "Node 3"};
    private int selectedMinute = 0;
    SeekBar battery_warning, moisture_warning, humidity_warning, temp_warning;
    TextView battery, moisture, humidity, temperature;
    private User_node_info api_get_node_info;
    private String BaseUrl="https://digitaldetectives.top/App/Main/";
    Dialog dialog_settings, dialog_device_list, add_sensor_motor;
    SeekBar seekBar_setBumpTime;
    String selectedItem = "";
    int current_battery, current_humidity, current_moisture, current_temperature;
    Spinner spinner;
    Spinner sensor_list, motor_list;
    String username, garden, getPumpTime="";
    TextView username_settings, settings,device_list, seekbar_status;
    EditText setpumptime;
    TimePicker dailyPumptime;
    private String[] node_info = {"yolo","123"};
    private static String[] ssensor_list = {"not select","sensor 1", "sensor 2", "sensor 3"};
    private static String[] smotor_list = {"not select","motor 1", "motor 2","motor 3"};
    private static String[] garden_list  = {"yolo"};
    private String minute, hour;
    private String current_dailyPump;
    String selectedMotor="",selectedSensor="";
    private int index1 =0;

    ListView list_connected;
    ArrayList<String > data_list;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a__settings, container, false);


        username_settings= view.findViewById(R.id.username_settings);

        signout = view.findViewById(R.id.signout_settings);
        settings = view.findViewById(R.id.settings_settings);
        device_list = view.findViewById(R.id.device_list);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String usernameLogin = sharedPreferences.getString("usernameLogin", "");


        String usernameSignup = sharedPreferences.getString("usernameSignup", "");


        if (usernameSignup.isEmpty()) {
            username = usernameLogin;


        }
        if (usernameLogin.isEmpty()){
            username = usernameSignup;

        }

        getNodename(new OnGetNodeNameListener() {
            @Override
            public void onGetNodeNameSuccess(String[] items) {
                ArrayList<String> updatedGardenList = new ArrayList<>(Arrays.asList(garden_list));
                updatedGardenList.remove("yolo");

                for (int i = 0; i < items.length; i++) {

                    updatedGardenList.add(items[i]);
                    garden_list= updatedGardenList.toArray(new String[0]);
                    Log.d("yolo123456",items[i]);
                    //Log.d("yolo123456",garden_list[i]);
                }

            }

            @Override
            public void onGetNodeNameError() {
                String[] yo = {"No data"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text, yo);
                spinner.setAdapter(adapter);
            }
        });


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


                spinner = (Spinner) dialog_settings.findViewById(R.id.spinner_node);
                dailyPumptime = (TimePicker) dialog_settings.findViewById(R.id.simpleTimePicker) ;
                setpumptime = (EditText) dialog_settings.findViewById(R.id.edt_pump);
                btn_ok = (Button) dialog_settings.findViewById(R.id.btn_settings_ok);
                battery_warning = (SeekBar) dialog_settings.findViewById(R.id.seekBar_battery);
                humidity_warning = (SeekBar) dialog_settings.findViewById(R.id.seekBar_humidity);
                moisture_warning = (SeekBar) dialog_settings.findViewById(R.id.seekBar_moisture);
                temp_warning = (SeekBar) dialog_settings.findViewById(R.id.seekBar_temp);
                switch_daily_pump = (Switch) dialog_settings.findViewById(R.id.sw_daily_pump);
                overlay = (View) dialog_settings.findViewById(R.id.overlay);
                time_picker=(LinearLayout) dialog_settings.findViewById(R.id.time_picker);

                temperature = (TextView) dialog_settings.findViewById(R.id.text_view_temp_warning);
                battery = (TextView) dialog_settings.findViewById(R.id.text_view_battery_warning);
                humidity = (TextView) dialog_settings.findViewById(R.id.textView_humidity);
                moisture = (TextView) dialog_settings.findViewById(R.id.textView_moisture);

//                String[] items = {"Vietnamese", "English"};
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text_settings, items);
//                spinner_setLanguage.setAdapter(adapter);

                battery.setText("0%");
                humidity.setText("0%");
                moisture.setText("0%");

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String garden1 = sharedPreferences.getString("garden","");
                String usernameLogin = sharedPreferences.getString("usernameLogin", "");
                String usernameSignup = sharedPreferences.getString("usernameSignup", "");


//                dailyPumptime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//                    @Override
//                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                        selectedHour = hourOfDay;
//                        selectedMinute = minute;
//                        String yolo = String.valueOf(selectedHour)+":"+String.valueOf(selectedMinute);
//                        editor.putString("dailypumptime",yolo);
//                        editor.apply();
//                    }
//                });


                if (usernameSignup.isEmpty()) {
                    username = usernameLogin;
                }
                if (usernameLogin.isEmpty()){
                    username = usernameSignup;
                }
//                getNodeInfo(garden1, new OnGetNodeInfoListener() {
//                    @Override
//                    public void onGetNodeInfoSuccess(String[] node_info) {
//                        Node_info nodeInfo = new Node_info(node_info);
//                        getPumpTime= nodeInfo.getBumpTime();
//
//                    }
//
//                    @Override
//                    public void onGetNodeInfoError() {
//                        Toast.makeText(getContext(), "Network error " , Toast.LENGTH_SHORT).show();
//                    }
//                });


//                btn_ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String pumpTime = String.valueOf(x);
//
//                        Retrofit retrofit=new Retrofit.Builder()
//                                .baseUrl("https://digitaldectectives.azdigi.blog/setting/")
//                                .addConverterFactory(GsonConverterFactory.create())
//                                .build();
//                        api_set_pump_time = retrofit.create(Set_pump_time.class);
//                        Toast.makeText(getContext(), username+garden1+pumpTime,Toast.LENGTH_SHORT).show();
//                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//
//                        String yolo = sharedPreferences.getString("dailypumptime","");
////                        Call<PumpTime> change_pump_time = api_set_pump_time.set_pump_time(username,garden1, pumpTime,yolo);
////
////
////                        change_pump_time.enqueue(new Callback<PumpTime>() {
////                            @Override
////                            public void onResponse(Call<PumpTime> call, Response<PumpTime> response) {
////
////                            }
////
////                            @Override
////                            public void onFailure(Call<PumpTime> call, Throwable t) {
////
////                            }
////                        });
//
//                        dialog_settings.dismiss();
//                    }
//
//                });           u

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

//              Van dang loi cai luu do dung sharePreference gi do ....
//
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        selectedItem = items[i];
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("settings_garden",selectedItem);
                        editor.apply();
                        getNodeInfo(selectedItem, new OnGetNodeInfoListener() {
                            @Override
                            public void onGetNodeInfoSuccess(String[] node_info) {
                                Node_info nodeInfo = new Node_info(node_info);
                                getPumpTime= nodeInfo.getBumpTime();
                                setpumptime.setText(getPumpTime);

                                String dailyPumpTime = nodeInfo.getDaily_pumpTime();


                                if (dailyPumpTime.isEmpty()){
                                    dailyPumptime.setHour(12);
                                    dailyPumptime.setMinute(0);

                                    switch_daily_pump.setChecked(false);
                                    dailyPumptime.setAlpha(0.5f);
//                                    overlay.setVisibility(View.VISIBLE);
                                    time_picker.setEnabled(false);
                                }
                                else {
                                    dailyPumptime.setAlpha(1.0f);
//                                    overlay.setVisibility(View.GONE);
                                    switch_daily_pump.setChecked(true);
                                    time_picker.setEnabled(true);

                                    minute= dailyPumpTime.substring(dailyPumpTime.length()-2);
                                    hour = dailyPumpTime.substring(0,dailyPumpTime.length()-2);

//                                    Toast.makeText(getContext(), hour +" yo " +minute,Toast.LENGTH_SHORT).show();

                                    dailyPumptime.setHour(Integer.valueOf(hour));
                                    dailyPumptime.setMinute(Integer.valueOf(minute));
                                }

                                battery_warning.setProgress(Integer.parseInt(nodeInfo.getBattery_warning()));
                                battery.setText(nodeInfo.getBattery_warning() + "%");

                                humidity_warning.setProgress(Integer.parseInt(nodeInfo.getAirWarning()));
                                humidity.setText(nodeInfo.getAirWarning()+"%");

                                moisture_warning.setProgress(Integer.parseInt(nodeInfo.getSoilWarning()));
                                moisture.setText(nodeInfo.getSoilWarning()+"%");

                                temp_warning.setProgress(Integer.parseInt(nodeInfo.getTempWarning()));
                                temperature.setText(nodeInfo.getTempWarning()+"°C");



                            }

                            @Override
                            public void onGetNodeInfoError() {
//                                Toast.makeText(getContext(), "Network error " , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                switch_daily_pump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            dailyPumptime.setAlpha(1.0f);
//                                    overlay.setVisibility(View.GONE);
                            switch_daily_pump.setChecked(true);
                            time_picker.setEnabled(true);


                            dailyPumptime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                                @Override
                                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                                    selectedHour = hourOfDay;
                                    selectedMinute = minute;
                                    current_dailyPump = String.valueOf(selectedHour)+String.valueOf(selectedMinute);
//                        Toast.makeText(getContext(),yolo, Toast.LENGTH_SHORT).show();
                                    editor.putString("dailypumptime",current_dailyPump);
                                    editor.apply();
                                }
                            });
                        }
                        else{
                            switch_daily_pump.setChecked(false);
                            dailyPumptime.setAlpha(0.5f);
//                                    overlay.setVisibility(View.VISIBLE);
                            time_picker.setEnabled(false);
                            editor.putString("dailypumptime","");
                            editor.apply();
                        }
                    }
                });



                battery_warning.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        battery.setText(String.valueOf(i)+"%");
                        current_battery = i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("battery_warning",String.valueOf(current_battery));
                        editor.apply();
                    }
                });
                humidity_warning.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        humidity.setText(String.valueOf(i)+"%");
                        current_humidity = i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("humidity_warning", String.valueOf(current_humidity));
                        editor.apply();
                    }
                });
                moisture_warning.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        moisture.setText(String.valueOf(i)+"%");
                        current_moisture = i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("moisture_warning", String.valueOf(current_moisture));
                        editor.apply();
                    }
                });
                temp_warning.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        temperature.setText(String.valueOf(i)+"°C");
                        current_temperature = i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("temp_warning", String.valueOf(current_temperature));
                        editor.apply();
                    }
                });



                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        String dailyPumpTime = sharedPreferences.getString("dailypumptime","");
                        String pumptime = setpumptime.getText().toString();
                        String battery = sharedPreferences.getString("battery_warning","");
                        String moisture = sharedPreferences.getString("moisture_warning","");
                        String temp1 = sharedPreferences.getString("temp_warning","");
                        String humidity = sharedPreferences.getString("humidity_warning","");
                        String garden = sharedPreferences.getString("settings_garden","");

//                        Toast.makeText(getContext(),selectedHour+selectedMinute+" "+pumptime+" "+battery +
//                                " "+ temp1 + " " +moisture+ " "+humidity+" "+garden,Toast.LENGTH_SHORT).show();

                        Retrofit retrofit_param_update = new Retrofit.Builder()
                                .baseUrl("https://digitaldetectives.top/App/Setting/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        api_param_update = retrofit_param_update.create(Set_pump_time.class);

////////////////////////////////////////////////////////////////////////

                        //Toast.makeText(getContext(), username+garden+pumptime+dailyPumpTime+humidity+moisture+temp1+battery, Toast.LENGTH_SHORT).show();

///////////////////////////////////////////////////////////////////////////

                        Call<PumpTime> update_param = api_param_update.paramUpdate(username,garden,pumptime,
                                dailyPumpTime,humidity,moisture,temp1,battery);
                        update_param.enqueue(new Callback<PumpTime>() {
                            @Override
                            public void onResponse(Call<PumpTime> call, Response<PumpTime> response) {
//                                Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PumpTime> call, Throwable t) {
//                                Toast.makeText(getContext(),"Failed to connect database", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog_settings.dismiss();
                    }
                });
                dialog_settings.show();
            }
        });

        device_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_device_list = new Dialog(getContext());
                dialog_device_list.setContentView(R.layout.device_list_dialog1);
                add_device = (Button) dialog_device_list.findViewById(R.id.add_device);
                list_connected = (ListView) dialog_device_list.findViewById(R.id.listView);

                garden = null;
                getNodename(new OnGetNodeNameListener() {
                    @Override
                    public void onGetNodeNameSuccess(String[] items) {
                        ArrayList<String> updatedGardenList = new ArrayList<>(Arrays.asList(garden_list));
//                        updatedGardenList.remove("yolo");

                        if (items.length == 0 ){
                            updatedGardenList.add("No data");
                            garden_list = updatedGardenList.toArray(new String[0]);
                        }
                        else {
                            for (int i = 0; i < items.length; i++) {
                                updatedGardenList.add(items[i]);
                                garden_list= updatedGardenList.toArray(new String[0]);
                                Log.d("yolo123456",items[i]);
                                //Log.d("yolo123456",garden_list[i]);
                            }
                        }

                    }

                    @Override
                    public void onGetNodeNameError() {
                        String[] yo = {"No data"};
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text, yo);
                        spinner.setAdapter(adapter);
                    }
                });
                Window window = dialog_device_list.getWindow();
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(15); // Đặt bán kính bo góc
                window.setBackgroundDrawable(gradientDrawable);
                dialog_device_list.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
                dataList = new ArrayList<>();
                dataList.clear();


                if (!garden_list[0].equals("No data")) {
                    for (int i = 0; i < garden_list.length; i++){

                        final int index = i+1;

                        Log.d("yolo1234567",String.valueOf(index));


                        getNodeInfo(String.valueOf(index), new OnGetNodeInfoListener() {
                            @Override
                            public void onGetNodeInfoSuccess(String[] node_info) {
                                Node_info nodeInfo = new Node_info(node_info);
                                Log.d("yolo12345678",String.valueOf(index));

                                String[] newdata = {"Garden " + String.valueOf(index)+"   ",nodeInfo.getSensor_id(),nodeInfo.getMotor_id()};
                                dataList.add(newdata);
                                Collections.sort(dataList, new Comparator<String[]>() {
                                    @Override
                                    public int compare(String[] o1, String[] o2) {
                                        int i1 = Integer.parseInt(o1[0].split(" ")[1]); // Lấy chỉ số i từ dòng đầu tiên
                                        int i2 = Integer.parseInt(o2[0].split(" ")[1]); // Lấy chỉ số i từ dòng thứ hai

                                        return Integer.compare(i1, i2);
                                    }
                                });
                                customListAdapter = new CustomListAdapter(getContext(),dataList);
                                list_connected.setAdapter(customListAdapter);
                                customListAdapter.notifyDataSetChanged();


                            }

                            @Override
                            public void onGetNodeInfoError() {

                            }
                        });
                    }
                }


//                for (int i = 0; i < garden_list.length; i++){
//                    customListAdapter = new CustomListAdapter(getContext(),dataList);
//                    list_connected.setAdapter(customListAdapter);
//                    customListAdapter.notifyDataSetChanged();
//                }

                add_device.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        add_sensor_motor = new Dialog(getContext());
                        add_sensor_motor.setContentView(R.layout.add_device_dialog);

                        sensor_list = (Spinner) add_sensor_motor.findViewById(R.id.sensor_list);
                        motor_list = (Spinner) add_sensor_motor.findViewById(R.id.motor_list);
                        confirm_add_device =  (Button) add_sensor_motor.findViewById(R.id.confirm_add);

                        Window window = add_sensor_motor.getWindow();
                        GradientDrawable gradientDrawable = new GradientDrawable();
                        gradientDrawable.setCornerRadius(15); // Đặt bán kính bo góc
                        window.setBackgroundDrawable(gradientDrawable);
                        add_sensor_motor.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);


                        getSensorList(new OnGetSensorListListener() {
                            @Override
                            public void onGetSensorListSuccess(String[] ssensor_list) {
                                ssensor_list = Arrays.copyOf(ssensor_list, ssensor_list.length + 1);
                                ssensor_list[ssensor_list.length - 1] = "Not_select";
                                ArrayAdapter<String> adapter5 = new ArrayAdapter<>(getContext(), R.layout.spinner_text_list, ssensor_list);
                                sensor_list.setAdapter(adapter5);
                            }

                            @Override
                            public void onGetSensorListError() {

                            }
                        });
                        getMotorList(new OnGetMotorListListener() {
                            @Override
                            public void onGetMotorListSuccess(String[] smotor_list) {
                                smotor_list = Arrays.copyOf(smotor_list, smotor_list.length + 1);
                                smotor_list[smotor_list.length - 1] = "Not_select";
                                ArrayAdapter<String> adapter6 = new ArrayAdapter<>(getContext(), R.layout.spinner_text_list, smotor_list);
                                motor_list.setAdapter(adapter6);
                            }

                            @Override
                            public void onGetMotorListError() {

                            }
                        });



                        sensor_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedSensor = ssensor_list[i];

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        motor_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedMotor = smotor_list[i];

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        //Confirm_add_device

//                        confirm_add_device.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (selectedSensor.equals("not select") && selectedMotor.equals("not select")){
//
//                                    add_sensor_motor.dismiss();
//                                }
//                                else {
//                                    if (selectedMotor.equals("not select")){
//                                        ArrayList<String> updatedSensorList = new ArrayList<>(Arrays.asList(ssensor_list));
//                                        updatedSensorList.remove(selectedSensor);
//                                        ssensor_list = updatedSensorList.toArray(new String[0]);
//                                        ArrayAdapter<String> updatedAdapter5 = new ArrayAdapter<>(getContext(), R.layout.spinner_text_list, ssensor_list);
//                                        sensor_list.setAdapter(updatedAdapter5);
//
//                                        String[] newdata = {String.valueOf(number),selectedSensor,"not select"};
//                                        dataList.add(newdata);
//                                        number++;
//
//                                        customListAdapter = new CustomListAdapter(getContext(), dataList);
//                                        list_connected.setAdapter(customListAdapter);
//                                        customListAdapter.notifyDataSetChanged();
//
//                                        add_sensor_motor.dismiss();
//                                    }
//                                    else if (selectedSensor.equals("not select")){
//                                        ArrayList<String> updatedMotorList = new ArrayList<>(Arrays.asList(smotor_list));
//                                        updatedMotorList.remove(selectedMotor);
//                                        smotor_list = updatedMotorList.toArray(new String[0]);
//                                        ArrayAdapter<String> updatedAdapter6 = new ArrayAdapter<>(getContext(), R.layout.spinner_text_list, smotor_list);
//                                        motor_list.setAdapter(updatedAdapter6);
//
//                                        String[] newdata = {String.valueOf(number),"not select",selectedMotor};
//                                        dataList.add(newdata);
//
//                                        customListAdapter = new CustomListAdapter(getContext(),dataList);
//                                        list_connected.setAdapter(customListAdapter);
//                                        customListAdapter.notifyDataSetChanged();
//
//                                        add_sensor_motor.dismiss();
//                                    }
//                                    else{
//
//                                        ArrayList<String> updatedSensorList = new ArrayList<>(Arrays.asList(ssensor_list));
//                                        updatedSensorList.remove(selectedSensor);
//                                        ssensor_list = updatedSensorList.toArray(new String[0]);
//                                        ArrayAdapter<String> updatedAdapter5 = new ArrayAdapter<>(getContext(), R.layout.spinner_text_list, ssensor_list);
//                                        sensor_list.setAdapter(updatedAdapter5);
//
//                                        ArrayList<String> updatedMotorList = new ArrayList<>(Arrays.asList(smotor_list));
//                                        updatedMotorList.remove(selectedMotor);
//                                        smotor_list = updatedMotorList.toArray(new String[0]);
//                                        ArrayAdapter<String> updatedAdapter6 = new ArrayAdapter<>(getContext(), R.layout.spinner_text_list, smotor_list);
//                                        motor_list.setAdapter(updatedAdapter6);
//
//                                        String[] newdata = {String.valueOf(number)+".  ",selectedSensor,selectedMotor};
//                                        dataList.add(newdata);
//                                        number++;
//
//                                        customListAdapter = new CustomListAdapter(getContext(),dataList);
//                                        list_connected.setAdapter(customListAdapter);
//                                        customListAdapter.notifyDataSetChanged();
//                                        add_sensor_motor.dismiss();
//
//                                    }
//                                }
//
//                            }
//                        });
                        if (selectedSensor.equals("Not_select")) {
                            selectedSensor = "0";
                        }

                        if (selectedMotor.equals("Not_select")) {
                            selectedMotor = "0";
                        }

//                        if(selectedSensor.equals("Not_select") &&  selectedMotor.equals("Not_select")){
//                            //Toast.makeText(getContext(),"Please choose at least one device to create garden", Toast.LENGTH_SHORT).show();
//                            //confirm_add_device.setEnabled(false);
//                        }

                        confirm_add_device.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            Toast.makeText(getContext(), username+ selectedSensor+selectedMotor, Toast.LENGTH_SHORT).show();
                                Retrofit retrofit=new Retrofit.Builder()
                                        .baseUrl("https://digitaldetectives.top/App/List/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();


                                api_new_garden = retrofit.create(Add_new_garden.class);
                                Call<NewGarden> add_garden = api_new_garden.add_garden(username,selectedSensor,selectedMotor);
                                add_garden.enqueue(new Callback<NewGarden>() {
                                    @Override
                                    public void onResponse(Call<NewGarden> call, Response<NewGarden> response) {
//                                        Toast.makeText(getContext(),"Success", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<NewGarden> call, Throwable t) {
//                                        Toast.makeText(getContext(),"!Success", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                add_sensor_motor.dismiss();

                            }
                        });

                        add_sensor_motor.show();

                    }
                });


//                dataList = new ArrayList<>();
//                customListAdapter = new CustomListAdapter(getContext(), dataList);
//                list_connected.setAdapter(customListAdapter);


                dialog_device_list.show();
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
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl("https://digitaldetectives.top/App/User/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                api_token_update= retrofit.create(Token_update.class);
//                        Toast.makeText(Home.this, user_namen, Toast.LENGTH_SHORT).show();
                Call<Token> tokenCall = api_token_update.token_update(username,"");
                tokenCall.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {

                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {

                    }
                });
                // Xóa các Activity khác ra khỏi Stack và khởi động Activity Welcome_login
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // Kết thúc Fragment hiện tại
                getActivity().finish();
            }
        });

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String usernameLogin2 = sharedPreferences2.getString("usernameLogin", "");
        String usernameSignup2 = sharedPreferences2.getString("usernameSignup", "");

        if (usernameSignup2.isEmpty()) username_settings.setText(usernameLogin);
        if (usernameLogin2.isEmpty()) username_settings.setText(usernameSignup);

        return view;

    }
    public String[] getSsensor_list(){
        return ssensor_list;
    }
    public String[] getSmotor_list(){
        return smotor_list;
    }



    private void getNodename(OnGetNodeNameListener listener){

        Retrofit retrofit_getInfo = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api_get_node_name = retrofit_getInfo.create(User_node_name.class);

        Call<User_node> callyo = api_get_node_name.getNodes(username);

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

//                    Toast.makeText(getContext(), "Server error",Toast.LENGTH_LONG).show();
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
    public void getNodeInfo(String node_id,OnGetNodeInfoListener listenerg){

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
//                    Toast.makeText(getContext(), "Get data error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User_node_info_1> call, Throwable t) {
                //Toast.makeText(getContext(), "Connect error",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getSensorList(OnGetSensorListListener listener) {
        Retrofit retrofit_get_sensor_list = new Retrofit.Builder()
                .baseUrl("https://digitaldetectives.top/App/List/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api_get_sensor_list = retrofit_get_sensor_list.create(GetSensorList.class);

        Call<Sensor_list> cally = api_get_sensor_list.getSensor(username);

        cally.enqueue(new Callback<Sensor_list>() {
            @Override
            public void onResponse(Call<Sensor_list> call, Response<Sensor_list> response) {

                Gson gson = new Gson();
                String json = gson.toJson(response.body());

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray sensorArray = jsonObject.getJSONArray("sensor");

                    ssensor_list = new String[sensorArray.length()];

                    for (int i = 0; i < sensorArray.length(); i++) {
                        ssensor_list[i] = sensorArray.getString(i);
                    }

                    if (listener != null) {
                        listener.onGetSensorListSuccess(ssensor_list);
                    }

                } catch (JSONException e) {


                    e.printStackTrace();
//                    Toast.makeText(getContext(), "Server error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Sensor_list> call, Throwable t) {
                if (listener != null) {
                    listener.onGetSensorListError();
                }
            }
        });


    }

    private void getMotorList(OnGetMotorListListener listener) {
        Retrofit retrofit_get_motor_list = new Retrofit.Builder()
                .baseUrl("https://digitaldetectives.top/App/List/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api_get_motor_list = retrofit_get_motor_list.create(GetMotorList.class);
        Call<Motor_List> callb = api_get_motor_list.getMotor(username);
        callb.enqueue(new Callback<Motor_List>() {
            @Override
            public void onResponse(Call<Motor_List> call, Response<Motor_List> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response.body());

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray motorArray = jsonObject.getJSONArray("motor");

                    smotor_list = new String[motorArray.length()];

                    for (int i = 0; i < motorArray.length(); i++) {
                        smotor_list[i] = motorArray.getString(i);
                    }

                    if (listener != null) {
                        listener.onGetMotorListSuccess(smotor_list);
                    }

                } catch (JSONException e) {


                    e.printStackTrace();
//                    Toast.makeText(getContext(), "Server error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Motor_List> call, Throwable t) {
                if (listener != null) {
                    listener.onGetMotorListError();
                }
            }
        });

    }


}