package com.ub.akshay.nitkart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private static final String TAG = Login.class.getSimpleName();
    ProgressBar progressBar;
    private Button loginButton;
    private EditText user, pass;
    private TextView newUser, resetPassword;
    private String username, password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loginButton = (Button) findViewById(R.id.loginButton);
        user = (EditText) findViewById(R.id.usernameLogin);
        pass = (EditText) findViewById(R.id.passwordLogin);
        setInputs(true);
        newUser = (TextView) findViewById(R.id.newUserRegistration);
        resetPassword = (TextView) findViewById(R.id.forgotPassword);
        progressBar = (ProgressBar) findViewById(R.id.loginPageProgressBar);
        Log.d(TAG, "TTTTTTTTTTTTTTTt");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user1 = firebaseAuth.getCurrentUser();

                if (user1 != null) {

                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user1.getUid());

                      Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    progressBar.setVisibility(View.GONE);
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //    Toast.makeText(getApplicationContext(), "FFFFF " , Toast.LENGTH_LONG).show();

                    username = user.getText().toString().trim();
                    password = pass.getText().toString().trim();

                    if (username.isEmpty() ) {
                        Toast.makeText(getApplicationContext(), "Enter User Name!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Minimum lenght of password should be 6!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                        progressBar.setVisibility(View.VISIBLE);
                        setInputs(false);
                        signIn(username, password);
                }
            });

            resetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Login.this, forgotPassword.class));
                }
            });

            newUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newUserReg = new Intent(Login.this, newUser.class);
                    startActivity(newUserReg);
                    finish();
                }
            });
    }

    @Override
    public void onBackPressed() {
       finish();
        System.exit(0);
    }
    @Override
        public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }
       @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }
        public void signIn(String email, String password) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                                setInputs(true);
                            }
                           else {
                                progressBar.setVisibility(View.GONE);
                                Intent t = new Intent(Login.this, MainActivity.class);
                                startActivity(t);
                                finish();
                                // ...
                            }
                        }
                    });
        }

    private void setInputs(boolean val){
        user.setEnabled(val);
        pass.setEnabled(val);
    }
}




/*
remain
add user in other database .
verivicatio email
 */
