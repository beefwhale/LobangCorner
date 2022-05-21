package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Home homeFragment = new Home();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment).commit();
                        return true;
                }
                switch (item.getItemId()) {
                    case R.id.explore:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment).commit();
                        return true;
                }
                switch (item.getItemId()) {
                    case R.id.hc:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment).commit();
                        return true;
                }
                switch (item.getItemId()) {
                    case R.id.rc:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment).commit();
                        return true;
                }
                switch (item.getItemId()) {
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}