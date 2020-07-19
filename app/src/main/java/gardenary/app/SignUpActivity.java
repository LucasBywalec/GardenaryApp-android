package gardenary.app;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import gardenary.app.databinding.ActivitySigninBinding;
import gardenary.app.databinding.ActivitySignupBinding;


public class SignUpActivity extends AppCompatActivity {

    ActivitySignupBinding binding;

    int logCount = 0;

    private static final String TAG = "SignUpActivity";
    
    private FirebaseAuth mAuth;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private EditText Efirstname, Eemail, Epassword;
    private Button buttonSignUp;

    //function to style all the text
    private void styleAllText(){
        TextView alreadyAUser = findViewById(R.id.AlreadyUser);
        String text = "Already a user? Sign in";

        SpannableString SignInSs = new SpannableString(text);

        ClickableSpan SignInCs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent goTo = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(goTo);
                //W drugim argumencie Intent dajemy nazwę Activity, które chcemy wyświetlić
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.argb(255, 92, 92, 92));
                //w tym fragmencie trzeba zmienić kolor "sign up" ponieważ defaultowo klikalne stringi są zielone
            }
        };
        Log.d(TAG, "Created the cs");

        SignInSs.setSpan(SignInCs, 16, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SignInSs.setSpan(new StyleSpan(Typeface.BOLD), 16, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        alreadyAUser.setText(SignInSs);
        alreadyAUser.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_signup);

        binding = ActivitySignupBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);



        styleAllText();

        Efirstname = binding.editTextFirstname;
        Eemail = binding.EditTextEmail;
        Epassword = binding.EditTextPassword;


        binding.ButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = Efirstname.getText().toString();
                String email = Eemail.getText().toString();
                String password = Epassword.getText().toString();

                createAccount(firstname, email, password);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        onStart();
    }

    //this function checks if the user is already signed in
    @Override
    public void onStart() {

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser); TODO
    }

    public void createAccount(String firstname, String email, String password){
        Log.d(TAG, firstname);
        Log.d(TAG, email);
        Log.d(TAG, password);

        if(!validateForm(firstname, email, password)){
            Toast.makeText(getApplicationContext(), "Invalid informations given", Toast.LENGTH_SHORT).show();
            return;
        }
        if (logCount == 0) sendUserDataToDatabase(firstname, email);
        logCount++;
        //TODO
        //ProgressBar

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            //updateUI(user); TODO
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null); TODO
                        }
                        // TODO
                        // hideProgressBar
                    }
                });
    }

    private void sendUserDataToDatabase(String firstname, String email) {
        //getInstance jest wymagane, natomiast gerReference jest odwołaniem do odpowiedniego pliku
        Timestamp today = new Timestamp(System.currentTimeMillis());
        UserHandler user = new UserHandler(firstname, email, today);

        database.collection("users").add(user);

        Log.d(TAG, "Finish");
    }

    private boolean validateForm(String firstname, String email, String password) {
        if(TextUtils.isEmpty(firstname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            return false;
        }
        else return true;
    }
}