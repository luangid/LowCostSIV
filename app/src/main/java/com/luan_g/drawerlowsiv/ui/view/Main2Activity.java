package com.luan_g.drawerlowsiv.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.regions.Region;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.AttachPolicyRequest;
import com.amazonaws.services.iot.model.CreateThingRequest;
import com.amazonaws.services.iot.model.CreateThingResult;
import com.amplifyframework.core.Amplify;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.luan_g.drawerlowsiv.R;
import com.luan_g.drawerlowsiv.constants.MQTTConstants;
import com.luan_g.drawerlowsiv.databinding.ActivityMain2Binding;
import com.luan_g.drawerlowsiv.entity.User;
import com.luan_g.drawerlowsiv.ui.viewmodel.MainViewModel;

public class Main2Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;
    private MainViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.appBarMain2.toolbar);
        binding.appBarMain2.fab.setOnClickListener(v -> {
            startActivity(new Intent(this, FilesActivity.class));
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cam1, R.id.nav_cam2, R.id.nav_cam3, R.id.nav_cam4)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        this.setObservers();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.provisionDevice(this, MQTTConstants.MQTT_THING);
            this.mViewModel.setUser(new User(extras.get("username").toString(), extras.get("password").toString()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setObservers() {
        this.mViewModel.user.observe(this, user -> {
        });
        this.mViewModel.image.observe(this, bitmap -> {
        });
    }

    private void provisionDevice(Context context, String androidId) {
        AWSMobileClient mobileClient = (AWSMobileClient) Amplify.Auth.getPlugin("awsCognitoAuthPlugin").getEscapeHatch();
        AWSIotClient client = new AWSIotClient(mobileClient);
        client.setRegion(Region.getRegion("sa-east-1"));

        String deviceType = "ANDROID_DEVICE_TYPE";
        String policyName = "ANDROID_POLICY";

        mobileClient.initialize(context, new Callback<UserStateDetails>() {
            @Override
            public void onResult(final UserStateDetails details) {
                CreateThingRequest createThingRequest = new CreateThingRequest().withThingName(androidId);
                CreateThingResult newThing = client.createThing(createThingRequest);
                AttachPolicyRequest attachPolicyRequest = new AttachPolicyRequest().withPolicyName(policyName).withTarget(mobileClient.getIdentityId());
                client.attachPolicy(attachPolicyRequest);
            }

            @Override
            public void onError(final Exception e) {
                Log.e("iot_thing_error", "Failed to connect: ", e);
                e.printStackTrace();
            }
        });
    }
}