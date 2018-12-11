package tactilebike.cse.tamu.edu.tactilebike;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;

import com.mapbox.api.directions.v5.models.DirectionsRoute;

import java.lang.reflect.Field;

public class Entry extends AppCompatActivity {
    private HomeTab home_tab;
    private HealthTab health_tab;
    private ProfileTab profile_tab;
    private SettingsTab settings_tab;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = home_tab;
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_health:
                    fragment = health_tab;
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = profile_tab;
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_settings:
                    fragment = settings_tab;
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_tab = new HomeTab();
        health_tab = new HealthTab();
        profile_tab = new ProfileTab();
        settings_tab = new SettingsTab();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment fragment = new HomeTab();
        loadFragment(fragment);

        try {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < 4; i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }

            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            if (!sharedpreferences.contains("meters"))
            {
                editor.putFloat("meters",0);
                editor.apply();
                editor.commit();
            }
            System.out.println("Hello!");
        }
        catch (NoSuchFieldException nsfe)
        {

        } catch (IllegalAccessException iae) {

        }
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
