package com.luan_g.drawerlowsiv.ui.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.luan_g.drawerlowsiv.R;
import com.luan_g.drawerlowsiv.database.MQTTHandler;
import com.luan_g.drawerlowsiv.entity.ConnectionListener;

public class LogInActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        this.mViewHolder.editUser = findViewById(R.id.edit_username_log);
        this.mViewHolder.editPassword = findViewById(R.id.edit_password_log);
        this.mViewHolder.btnLogin = findViewById(R.id.btn_login);
        this.mViewHolder.btnSignup = findViewById(R.id.text_btn_signup);

        this.mViewHolder.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mViewHolder.editUser.getText().toString();
                String pass = mViewHolder.editPassword.getText().toString();
                Amplify.Auth.signIn(
                        user,
                        pass,
                        result -> {
                            if(result.isSignInComplete()){
                               connectMqtt(user, pass);
                            } else {
                                Toast.makeText(getApplicationContext(), "Login falhou", Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );
            }
        });
        this.mViewHolder.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConnectionActivity.class));
            }
        });

        this.mViewHolder.editUser.setText("luan");
        this.mViewHolder.editPassword.setText("1234teste");

        this.loadDataFromActivity();
    }

    private void connectMqtt(String user, String pass) {

        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        intent.putExtra("username", user);
        intent.putExtra("password", pass);
        startActivity(intent);

    }
    private void loadDataFromActivity(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String user = bundle.getString("username");
            String pass = bundle.getString("password");
            this.mViewHolder.editUser.setText(user);
            this.mViewHolder.editPassword.setText(pass);
        }
    }

    private class ViewHolder {
        Button btnLogin;
        TextView btnSignup;
        EditText editUser;
        EditText editPassword;
    }
}