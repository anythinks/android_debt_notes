package com.android.myapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.android.myapp.databinding.ActivityMainBinding;
import com.android.myapp.fragments.FragmentHome;
import com.android.myapp.fragments.FragmentSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ActivityMainBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = binding.bottomnavView;
        bottomNavigationView.setSelectedItemId(R.id.home);
        loadfragment(new FragmentHome());
        bottomNavigationView.getMenu()
                .findItem(R.id.bottom_navigation_home)
                .setIcon(R.drawable.ic_home_filled);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_navigation_home) {
                loadfragment(new FragmentHome());
                item.setIcon(R.drawable.ic_home_filled);
                bottomNavigationView.getMenu()
                        .findItem(R.id.bottom_navigation_settings)
                        .setIcon(R.drawable.ic_settings_outlined);
            } else if (item.getItemId() == R.id.bottom_navigation_settings) {
                loadfragment(new FragmentSettings());
                item.setIcon(R.drawable.ic_settings_filled);
                bottomNavigationView.getMenu()
                        .findItem(R.id.bottom_navigation_home)
                        .setIcon(R.drawable.ic_home_outlined);
            }
            return true;
        });
    }

    public void loadfragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.containerView, fragment)
                .addToBackStack(null)
                .commit();
    }
}