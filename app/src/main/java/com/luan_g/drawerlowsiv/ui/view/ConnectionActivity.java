package com.luan_g.drawerlowsiv.ui.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.luan_g.drawerlowsiv.R;

public class ConnectionActivity extends AppCompatActivity {
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        this.mViewHolder.btnSave = findViewById(R.id.btn_confirm);
        this.mViewHolder.editUser = findViewById(R.id.edit_username);
        this.mViewHolder.editEmail = findViewById(R.id.edit_email);
        this.mViewHolder.editPassword = findViewById(R.id.edit_password);
        this.mViewHolder.editConfirmPassword = findViewById(R.id.edit_confirm_password);


        //EXCLUIR ESSAS DUAS LINHAS
        this.mViewHolder.editUser.setText("luan");
        this.mViewHolder.editEmail.setText("luanstohrqui@gmail.com");
        this.mViewHolder.editPassword.setText("1234teste");
        this.mViewHolder.editConfirmPassword.setText("");


        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );

        this.setListeners();

    }

    private void setListeners() {
        this.mViewHolder.btnSave.setOnClickListener(e -> {
            String username = mViewHolder.editUser.getText().toString();
            String email = mViewHolder.editEmail.getText().toString();
            String password = mViewHolder.editPassword.getText().toString();
            String confPass = mViewHolder.editConfirmPassword.getText().toString();
            //Realizar confirmação de senha.
            if(!password.equals(confPass)){
                Toast.makeText(getApplicationContext(), "Senhas não conferem!", Toast.LENGTH_SHORT).show();
                return;
            }
            AuthSignUpOptions options = AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.email(), email)
                    .build();
            Amplify.Auth.signUp(username, password, options,
                    result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                    error -> Log.e("AuthQuickStart", "Sign up failed", error)
            );
            createAlertDialog(username, password).show();

        });
    }
    private AlertDialog createAlertDialog(String username, String password){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_confir_code_layout, null);
        EditText editText = view.findViewById(R.id.edit_confirm_code);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.confir_code)
                .setView(view)
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String confCode = editText.getText().toString();
                        Amplify.Auth.confirmSignUp(
                                username,
                                confCode,
                                result -> {
                                    if(result.isSignUpComplete()){
                                        openLogin(username, password);
                                    }
                                },
                                error -> Log.e("AuthQuickstart", error.toString())
                        );
                    }
                })
                .setNegativeButton("Voltar", (dialogInterface, i) -> {

                }).setCancelable(false);
        return dialogBuilder.create();
    }
    private void openLogin(String username, String password){
        Amplify.Auth.signIn(
                username,
                password,
                result -> {
                    if(result.isSignInComplete()){
                        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
        Toast.makeText(this, "Texto", Toast.LENGTH_SHORT).show();
    }
    
    
    private class ViewHolder {
        Button btnSave;
        EditText editUser;
        EditText editEmail;
        EditText editPassword;
        EditText editConfirmPassword;
    }
}