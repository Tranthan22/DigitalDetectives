package com.example.myapplication2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class A_Fragment_Adapter extends FragmentStateAdapter {
    public A_Fragment_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new A_Home();

            case 1:
                return new A_Notification();
            default:
                return new A_Settings();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
