package com.luan_g.drawerlowsiv;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.AmplifyConfiguration;

public class DrawerLowSIV extends Application {
    public void onCreate() {
        super.onCreate();

        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(AmplifyConfiguration.builder(getApplicationContext()).devMenuEnabled(false).build(), getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

    }
}
