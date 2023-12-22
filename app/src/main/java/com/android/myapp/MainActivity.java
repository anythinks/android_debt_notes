package com.android.myapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        bottomNavigationView = binding.bottomnavView;
        bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_home);
        loadfragment(new FragmentHome());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_navigation_home) {
                loadfragment(new FragmentHome());
                item.setIcon(R.drawable.ic_home_filled);
                setIconUnselectedBottomNav(
                        R.id.bottom_navigation_settings,
                        R.drawable.ic_settings_outlined);
            } else if (item.getItemId() == R.id.bottom_navigation_settings) {
                loadfragment(new FragmentSettings());
                item.setIcon(R.drawable.ic_settings_filled);
                setIconUnselectedBottomNav(
                        R.id.bottom_navigation_home,
                        R.drawable.ic_home_outlined);
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        var homeItem = bottomNavigationView.getMenu().getItem(0).getItemId();
        if (homeItem != bottomNavigationView.getSelectedItemId()) {
            bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_home);
            return;
        }
        super.onBackPressed();
    }

    public void loadfragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerView, fragment)
                .commit();
    }

    private void setIconUnselectedBottomNav(int item, int notActiveIcon) {
        bottomNavigationView.getMenu()
                .findItem(item)
                .setIcon(notActiveIcon);
    }
}