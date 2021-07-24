package com.nkr.mashproadmin.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.nkr.mashproadmin.R;
import com.nkr.mashproadmin.databinding.ActivityMainBinding;
import com.nkr.mashproadmin.ui.userCredential.UserSubscriptionPlanViewModel;
import com.nkr.mashproadmin.util.SharedPrefsHelper;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import kotlin.Lazy;

import static androidx.navigation.Navigation.findNavController;
import static com.nkr.mashproadmin.util.ConstantsKt.USER_TYPE;
import static com.nkr.mashproadmin.util.ConstantsKt.USER_TYPE_CREATOR;
import static com.nkr.mashproadmin.util.ConstantsKt.USER_TYPE_VIEWER;
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
        inflateCreatorNavView(navView,navController);

        navController.addOnDestinationChangedListener(
                this
        );


    }

    private void inflateCreatorNavView(BottomNavigationView navView, NavController navController) {
      Log.i("user_type","user_type : Creator");
        navView.getMenu().clear();
        navView.inflateMenu(R.menu.bottom_nav_creator_menu);
        navController.setGraph(R.navigation.mobile_navigation_creator);
        NavigationUI.setupWithNavController(
                navView, navController);
    }


    @Override
    public void onDestinationChanged(@NonNull @NotNull NavController controller,
                                     @NonNull @NotNull NavDestination destination,
                                     @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {


        if (destination.getId() == R.id.authenticationFragment2 || destination.getId() == R.id.userTypeFragment2) {
            Objects.requireNonNull(getSupportActionBar()).hide();
            binding.navView.setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(getSupportActionBar()).show();
            binding.navView.setVisibility(View.VISIBLE);
        }

    }

}