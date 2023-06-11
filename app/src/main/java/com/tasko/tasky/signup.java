package com.tasko.tasky;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {
    public signup(){}

    public signup(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String uname) {
        this.username = uname;
    }

    public String getEmail() { return email;}

    public void setEmail(String mail) { this.email = mail;}

    private String username;

    private String email;
    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(signup.this,dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        DbManager manager = new DbManager(this, "userdb", null, 1);
        EditText Name=findViewById(R.id.Name);
        username=Name.getText().toString();
        EditText Email = findViewById(R.id.Email);
        EditText Pass = findViewById(R.id.Pass);
        EditText Pass2 = findViewById(R.id.Pass2);
        TextView logtext = findViewById(R.id.LoginText);
        ProgressBar prog=findViewById(R.id.progressBar);
        Button Signup = findViewById(R.id.Signup);
        mAuth = FirebaseAuth.getInstance();
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=Name.getText().toString();
                email = String.valueOf(Email.getText());
                prog.setVisibility(View.VISIBLE);
                String password, passwordc;
                password = String.valueOf(Pass.getText());
                passwordc = String.valueOf(Pass2.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(signup.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(signup.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.equals(password, passwordc)) {
                    manager.addUserData(new signup(username, email));
                    manager.close();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    prog.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "createUserWithEmail:success");
                                        Intent intent = new Intent(signup.this, dashboard.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(signup.this,
                                                "Authentication failed. Password length should be at least 8 characters long",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                } else{
                    Toast.makeText(signup.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
            }

        });

        logtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}












