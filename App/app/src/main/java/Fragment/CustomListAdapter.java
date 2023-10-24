package Fragment;

import static android.content.Context.MODE_PRIVATE;
import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication2.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import Device_info.GetMotorList;
import Device_info.GetSensorList;
import Device_info.Motor_List;
import Device_info.OnGetMotorListListener;
import Device_info.OnGetSensorListListener;
import Device_info.Sensor_list;
import Device_info.Update_list;
import Device_info.Update_list_info;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomListAdapter extends ArrayAdapter<String []> {
    private ArrayList<String[] > dataList;
    A_Settings fragment_settings;
    private GetSensorList api_get_sensor_list;
    private GetMotorList api_get_motor_list;

    private Update_list api_update_list;

    Dialog edit_connected_device_dialog;
    Button confirmButton;
    Spinner sensor_list_edit, motor_list_edit;
    private static String confirm_sensor, confirm_motor;
    private String[] received_Sensor_List={""},received_Motor_List={""};
    private String username;
    private Context mcontext;

    private static String[] sensor_list = {"yolo"};

    private static String[] motor_list = {""};
    public CustomListAdapter(Context context, ArrayList<String []> dataList) {
        super(context, R.layout.list_item_layout, dataList);
        this.dataList = dataList;
        mcontext =context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }

        fragment_settings = new A_Settings();

        TextView sensorTextView = itemView.findViewById(R.id.sensorTextView);
        TextView motorTextView = itemView.findViewById(R.id.motorTextView);
        TextView numberTextView = itemView.findViewById(R.id.number);
        Button editButton = itemView.findViewById(R.id.editButton);




        String[] item = getItem(position);
        numberTextView.setText(item[0]);
        sensorTextView.setText(item[1]);
        motorTextView.setText(item[2]);



        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String usernameLogin = sharedPreferences.getString("usernameLogin", "");


        String usernameSignup = sharedPreferences.getString("usernameSignup", "");


        if (usernameSignup.isEmpty()) {
            username = usernameLogin;


        }
        if (usernameLogin.isEmpty()){
            username = usernameSignup;

        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               edit_connected_device_dialog = new Dialog(getContext());
               edit_connected_device_dialog.setContentView(R.layout.edit_connected_device);

                Window window = edit_connected_device_dialog.getWindow();
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(15); // Đặt bán kính bo góc
                window.setBackgroundDrawable(gradientDrawable);
                edit_connected_device_dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);


                sensor_list_edit =(Spinner) edit_connected_device_dialog.findViewById(R.id.sensor_list_edit) ;
                motor_list_edit = (Spinner) edit_connected_device_dialog.findViewById(R.id.motor_list_edit);
                confirmButton =(Button) edit_connected_device_dialog.findViewById(R.id.confirm_edit);




                getSensorList(new OnGetSensorListListener() {
                    @Override
                    public void onGetSensorListSuccess(String[] ssensor_list) {

//                        ArrayList<String> update_old = new ArrayList<>(Arrays.asList(ssensor_list));
                        ArrayList<String> update_new = new ArrayList<>(Arrays.asList(sensor_list));


//                        update_old.add(sensorTextView.getText().toString());
//                        update_old.add("Not_select");
//                        ssensor_list = update_old.toArray(new String[0]);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        update_new.remove("yolo");

                        sensor_list = new String[ssensor_list.length];
                        for (int i = 0; i < ssensor_list.length; i++) {
                            sensor_list[i] = ssensor_list[i];
                        }

                        update_new.add(sensorTextView.getText().toString());
                        update_new.add("Not_select");
                        sensor_list = update_new.toArray(new String[0]);


//                        Toast.makeText(getContext(), String.valueOf(ssensor_list.length), Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> updatedAdapter5 = new ArrayAdapter<>(getContext(), R.layout.spinner_text_list, sensor_list);
                        sensor_list_edit.setAdapter(updatedAdapter5);
                    }

                    @Override
                    public void onGetSensorListError() {

                    }
                });

                getMotorList(new OnGetMotorListListener() {
                    @Override
                    public void onGetMotorListSuccess(String[] smotor_list) {

                        ArrayList<String> updatedMotorList = new ArrayList<>(Arrays.asList(smotor_list));

                        ArrayList<String> updatedMotor1List = new ArrayList<>(Arrays.asList(motor_list));

 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        updatedMotorList.add((motorTextView.getText().toString()));
                        updatedMotorList.add("Not_select");

                        smotor_list = updatedMotorList.toArray(new String[0]);
                        motor_list = new String[smotor_list.length];



                        for (int i = 0; i < smotor_list.length; i++) {

                            motor_list[i] = smotor_list[i];
                        }
//                        Toast.makeText(getContext(), String.valueOf(smotor_list.length), Toast.LENGTH_SHORT).show();

                        ArrayAdapter<String> updateAdapter6 = new ArrayAdapter<>(getContext(),R.layout.spinner_text_list,motor_list);
                        motor_list_edit.setAdapter(updateAdapter6);
                    }

                    @Override
                    public void onGetMotorListError() {

                    }
                });



                sensor_list_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (i==sensor_list.length-1){
                            confirm_sensor ="0";
                        }
                        else {
                            confirm_sensor = sensor_list[i];
                        }

//                        Toast.makeText(getContext(), confirm_sensor+sensor_list.length, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                motor_list_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                        Toast.makeText(getContext(), confirm_motor+sensor_list.length, Toast.LENGTH_SHORT).show();

                        if (i==motor_list.length-1){
                            confirm_motor="0";
                        }
                        else {
                            confirm_motor = motor_list[i];
                        }
                        Toast.makeText(getContext(), confirm_motor, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                /////////////////////////////////////////////////////

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("yolo12345",confirm_motor+confirm_sensor);

                        // đổi giá trị sensorTextView và motorTextView sau khi đã thay đổi

                        dataList.get(position)[1] = confirm_sensor;
                        dataList.get(position)[2] = confirm_motor;

                        notifyDataSetChanged();

                        Retrofit retrofit_update_list = new Retrofit.Builder()
                                .baseUrl("https://digitaldetectives.top/App/List/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        api_update_list=retrofit_update_list.create(Update_list.class);
                        String garden = numberTextView.getText().toString().substring(7);

                        garden =garden.substring(0, garden.length()-3);


                        //dang fix cung, lam loi vat v~

                        Toast.makeText(mcontext, username+garden+confirm_sensor+confirm_motor, Toast.LENGTH_SHORT).show();



                        Call<Update_list_info> update_list_infoCall = api_update_list.update_list(username,garden,confirm_sensor,confirm_motor);

                        //Toast.makeText(getContext(),String.valueOf(position)+"  "+confirm_sensor+"   "+confirm_motor, Toast.LENGTH_SHORT).show();

                        update_list_infoCall.enqueue(new Callback<Update_list_info>() {
                            @Override
                            public void onResponse(Call<Update_list_info> call, Response<Update_list_info> response) {
                                Toast.makeText(getContext(),"Success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Update_list_info> call, Throwable t) {
                                Toast.makeText(getContext(),"!Success", Toast.LENGTH_SHORT).show();
                            }
                        });

                        edit_connected_device_dialog.dismiss();
                        // Toast.makeText(getContext(),confirm_sensor + confirm_motor,Toast.LENGTH_SHORT).show();


                    }
                });


               edit_connected_device_dialog.show();
            }
        });

        return itemView;
    }
    public void receivedSensorList(A_Settings fragment){
        received_Sensor_List = fragment.getSsensor_list();
    }
    public void receivedMotorList(A_Settings fragment_settings){
        received_Motor_List = fragment_settings.getSmotor_list();
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

                    sensor_list = new String[sensorArray.length()];

                    for (int i = 0; i < sensorArray.length(); i++) {
                        sensor_list[i] = sensorArray.getString(i);
                    }

                    if (listener != null) {
                        listener.onGetSensorListSuccess(sensor_list);
                    }

                } catch (JSONException e) {


                    e.printStackTrace();
                    Toast.makeText(getContext(), "Server error", Toast.LENGTH_LONG).show();
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

                    motor_list = new String[motorArray.length()];

                    for (int i = 0; i < motorArray.length(); i++) {
                        motor_list[i] = motorArray.getString(i);
                    }

                    if (listener != null) {
                        listener.onGetMotorListSuccess(motor_list);
                    }

                } catch (JSONException e) {


                    e.printStackTrace();
                    Toast.makeText(getContext(), "Server error", Toast.LENGTH_LONG).show();
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

