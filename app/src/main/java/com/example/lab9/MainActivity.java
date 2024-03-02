package com.example.lab9;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private Button login_btn;
    private TextView main_text;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_btn = (Button) findViewById(R.id.login_btn);
        main_text = (TextView) findViewById(R.id.mainText);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = gsc.getSignInIntent();
                GoogleSignInLauncher.launch(intent);
            }
        });
    }
    ActivityResultLauncher<Intent> GoogleSignInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                task.getResult(ApiException.class);
                Toast toast = Toast.makeText(getApplicationContext(), "Вход успешен", Toast.LENGTH_LONG);
                toast.show();

                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if (account != null) {
                    String name = account.getDisplayName();
                    String email = account.getEmail();
                    main_text.setText("Добро пожаловать, " + name + "\n(" + email + ")");
                    main_text.setVisibility(View.VISIBLE);
                    login_btn.setVisibility(View.INVISIBLE);
                }

            } catch (ApiException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Ошибка входа", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    });
}