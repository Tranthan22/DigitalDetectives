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


public class A_Settings extends Fragment {
    Button signout,icon_settings, btn_ok;
    private int x,g;
    Dialog dialog_settings;
    SeekBar seekBar_setBumpTime;
    Spinner spinner_setLanguage;
    TextView username_settings, settings, seekbar_status;



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
                String pumpTime = sharedPreferences.getString("pumpTime"+garden1,"");
                if (pumpTime.isEmpty()){
                    g= 0;
                }else {
                    g = Integer.valueOf(pumpTime);
                }

                seekbar_status.setText(g+ " minutes");
                seekBar_setBumpTime.setProgress(g);
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
                        Toast.makeText(getContext(),pumpTime, Toast.LENGTH_SHORT).show();
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
}