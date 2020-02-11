package aregmi.ramapo.edu.storyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText user_email;
    private EditText user_password;
    private Button user_login_button;
    private Button user_signup_button;

    //for authentication of users to use back-end services of the app
    private FirebaseAuth firebase_auth;
    //Listener called when there is change in authentication state.
    private FirebaseAuth.AuthStateListener auth_change_listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        user_login_button = findViewById(R.id.user_login);
        user_signup_button = findViewById(R.id.user_signup);

        //get current state of authentication status;
        firebase_auth = FirebaseAuth.getInstance();
        //When logged in and logged out, AuthStateListener is called. Use it to redirect users to logged in activity page.

        auth_change_listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                if (current_user!= null){
                    //Get the user ID and go to logged in intent
                    String user_id = firebaseAuth.getCurrentUser().getUid();
                    Intent home_page_intent = new Intent(MainActivity.this, HomeActivity.class);
                    home_page_intent.putExtra("user_id", user_id);
                    startActivity(home_page_intent);
                    finish();
                    return;
                }
            }
        };

        user_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get entered email and password
                String email = user_email.getText().toString();
                String password = user_password.getText().toString();

                //Call the SignInWithEmailAndPassword which Asynchronously signs the user in.
                //Firebase method.

                firebase_auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Error in signing in!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        user_signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = user_email.getText().toString();
                String password = user_password.getText().toString();

                firebase_auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Error in signing up! ", Toast.LENGTH_SHORT).show();
                                }

                                else{
                                    //GET userID assigned during registration
                                    String user_id = firebase_auth.getCurrentUser().getUid();

                                    //Add the user's ID under users Tree within the app's main database root
                                    DatabaseReference dbref_under_users = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                    dbref_under_users.setValue(true);
                                }
                            }
                        });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase_auth.addAuthStateListener(auth_change_listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //When leaving the activity, remove the listener.
        firebase_auth.removeAuthStateListener(auth_change_listener);
    }
}
