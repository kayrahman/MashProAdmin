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
import com.nkr.mashpro.util.SharedPrefsHelper;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import kotlin.Lazy;
import timber.log.Timber;

import static androidx.navigation.Navigation.findNavController;
import static com.nkr.mashpro.util.ConstantsKt.USER_TYPE;
import static com.nkr.mashpro.util.ConstantsKt.USER_TYPE_CREATOR;
import static com.nkr.mashpro.util.ConstantsKt.USER_TYPE_VIEWER;
import static org.koin.java.KoinJavaComponent.inject;

public class MainActivity2 extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private ActivityMainBinding binding;
    private Lazy<UserSubscriptionPlanViewModel> viewModel = inject(UserSubscriptionPlanViewModel.class);

    private SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefsHelper = new SharedPrefsHelper(this);

        BottomNavigationView navView = binding.navView;
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);

        NavController navController = navHostFragment.getNavController();

        viewModel.getValue().getUserType().observe(this, user_type->{
            if(user_type == USER_TYPE_CREATOR){
                navView.getMenu().clear();
                navView.inflateMenu(R.menu.bottom_nav_creator_menu);
                navController.setGraph(R.navigation.mobile_navigation_creator);

                NavigationUI.setupWithNavController(
                        navView, navController);

            }else if(user_type == USER_TYPE_VIEWER){
                navView.getMenu().clear();
                navView.inflateMenu(R.menu.bottom_nav_menu);
                navController.setGraph(R.navigation.mobile_navigation);
                NavigationUI.setupWithNavController(
                        navView, navController);

            }else{
                Integer type = sharedPrefsHelper.get(USER_TYPE,0);
                if(type == USER_TYPE_CREATOR){
                    navView.getMenu().clear();
                    navView.inflateMenu(R.menu.bottom_nav_creator_menu);
                    navController.setGraph(R.navigation.mobile_navigation_creator);

                    NavigationUI.setupWithNavController(
                            navView, navController);

                }else if(type == USER_TYPE_VIEWER){
                    navView.getMenu().clear();
                    navView.inflateMenu(R.menu.bottom_nav_menu);
                    navController.setGraph(R.navigation.mobile_navigation);
                    NavigationUI.setupWithNavController(
                            navView, navController);
                }
            }

        });


    //    AppBarConfiguration appBarConfiguration =
      //          new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();



        navController.addOnDestinationChangedListener(
                this
        );


    }

    @Override
    public void onDestinationChanged(@NonNull @NotNull NavController controller,
                                     @NonNull @NotNull NavDestination destination,
                                     @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {


        if (destination.getId() == R.id.authenticationFragment || destination.getId() == R.id.userTypeFragment2) {
            Objects.requireNonNull(getSupportActionBar()).hide();
          //  binding.navView.setVisibility(View.GONE);
        }

        else {
            Objects.requireNonNull(getSupportActionBar()).show();
            binding.navView.setVisibility(View.VISIBLE);
        }

    }

}