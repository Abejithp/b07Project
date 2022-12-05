package cscb07.group4.androidproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import cscb07.group4.androidproject.databinding.ActivityMainBinding;
import cscb07.group4.androidproject.manager.AccountManager;
import cscb07.group4.androidproject.manager.CourseManger;

public class MainActivity extends AppCompatActivity implements AccountChangeListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup bottom navbar
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        this.updateNavbar();
        navController.addOnDestinationChangedListener((navController1, destination, bundle) -> {
            if (destination.getId() == R.id.fragment_login || destination.getId() == R.id.fragment_register) {
                binding.bottomNavView.setVisibility(View.GONE);
            } else {
                binding.bottomNavView.setVisibility(View.VISIBLE);
            }
        });


        // These are the menus without the back button (top action bar)
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragment_timeline, R.id.fragment_edit_courses, R.id.fragment_admin_manage, R.id.nav_account)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        CourseManger.getInstance().refreshCourses();
        AccountManager.getInstance().registerListener(this);
    }

    @Override
    public void onAccountChange() {
        // Refresh navbar on account change
        this.updateNavbar();
    }

    public void updateNavbar() {
        // Different navbar depending on if the user is an admin or not
        binding.bottomNavView.getMenu().clear();
        if (AccountManager.getInstance().isAdmin()) {
            binding.bottomNavView.inflateMenu(R.menu.bottom_nav_menu_admin);
        } else {
            binding.bottomNavView.inflateMenu(R.menu.bottom_nav_menu);
        }
        NavHostFragment navController = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.bottomNavView, navController.getNavController());
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}