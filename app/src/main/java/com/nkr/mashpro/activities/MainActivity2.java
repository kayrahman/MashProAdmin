package com.nkr.mashpro.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nkr.mashpro.R;
import com.nkr.mashpro.databinding.ActivityMainBinding;
import com.nkr.mashpro.ui.userCredential.UserSubscriptionPlanViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import kotlin.Lazy;
import timber.log.Timber;

import static androidx.navigation.Navigation.findNavController;
import static org.koin.java.KoinJavaComponent.inject;

public class MainActivity2 extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private ActivityMainBinding binding;
    private Lazy<UserSubscriptionPlanViewModel> viewModel = inject(UserSubscriptionPlanViewModel.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = binding.navView;
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);

        NavController navController = navHostFragment.getNavController();

        viewModel.getValue().getUser_type().observe(this, user_type->{
            if(user_type == 801){
                navView.inflateMenu(R.menu.bottom_nav_creator_menu);
                navController.setGraph(R.navigation.mobile_navigation_creator);
            }else{
                navView.inflateMenu(R.menu.bottom_nav_menu);
                navController.setGraph(R.navigation.mobile_navigation);
            }
            Log.i("user_type",user_type.toString());
        });



        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();


        NavigationUI.setupWithNavController(
                navView, navController);

        navController.addOnDestinationChangedListener(
                this
        );


    }

    @Override
    public void onDestinationChanged(@NonNull @NotNull NavController controller,
                                     @NonNull @NotNull NavDestination destination,
                                     @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {

        if (destination.getId() == R.id.authenticationFragment) {
            Objects.requireNonNull(getSupportActionBar()).hide();
            binding.navView.setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(getSupportActionBar()).show();
            binding.navView.setVisibility(View.VISIBLE);
        }

    }

}