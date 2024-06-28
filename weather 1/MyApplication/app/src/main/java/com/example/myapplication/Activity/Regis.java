package com.example.myapplication.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.example.myapplication.R;

public class Regis extends AppCompatActivity {
    EditText rgemail, rgpass, rgcfpass;
    TextView log;
    Button regis;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regis);
        log = findViewById(R.id.txtlogin);
        rgemail = findViewById(R.id.txtregisemail);
        rgpass = findViewById(R.id.txtregispass);
        rgcfpass = findViewById(R.id.txtregisconfirmpass);
        regis = findViewById(R.id.btnregister);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        regis.setOnClickListener(v -> {
            String email = rgemail.getText().toString().trim();
            String password = rgpass.getText().toString().trim();
            String confirmPassword = rgcfpass.getText().toString().trim();

            if (isValidRegistration(email, password, confirmPassword)) {
                registerUser(email, password);
            } else {
                Toast.makeText(Regis.this, "Vui lòng kiểm tra lại thông tin đăng ký", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidRegistration(String email, String password, String confirmPassword) {
        return isValidEmail(email) && isValidPassword(password) && password.equals(confirmPassword);
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches("\\A.*(?=.*[a-zA-Z])(?=.*\\d).*\\z");
    }

    private void registerUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Regis.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    // Redirect the user to the main activity or a different activity
                } else {
                    Toast.makeText(Regis.this, "Đăng ký không thành công: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}