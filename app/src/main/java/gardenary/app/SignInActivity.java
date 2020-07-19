package gardenary.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Binder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import gardenary.app.databinding.ActivitySigninBinding;


public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private int signCount = 0;
    ActivitySigninBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private void styleAllText(){

        TextView textSignUp = binding.TextDontHaveAccount;
        //findViewById działa podobnie jak GetElement w js. Jednak argument passujący to klasa R, która łączy XMl z plikami .java

        String text = "Don’t have an account? Sign up.";
        SpannableString spannableSignUp = new SpannableString(text);
        //SpannableString to klasa w javie, która pozwala tworzyć mixowane style stringów

        //poniżej klasa tworząca klikalny fragmentu tekstu, z formą callbacka jako nową klasą
        ClickableSpan clickableSignUp = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent goTo = new Intent(SignInActivity.this, SignUpActivity.class);
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
        spannableSignUp.setSpan(clickableSignUp, 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableSignUp.setSpan(new StyleSpan(Typeface.BOLD), 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //dodanie klasy clickable to tekstu działą tak samo jak formatowanie i należy następnie jest "zset upować"
        //setSpan pozwala tworzyć ostylowany string. Pierwszy argument przyjmuje styl, jaki ma dany fragment przyjąć. Ostatni to typ zakresu
        textSignUp.setText(spannableSignUp);
        //na koniec należy ostylowany text na nowo wpisać w odpowiednie pole

        textSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        //linijka kodu wymagana, aby fragment textu był clickable
    }

    private void SignIn(){
        EditText email = binding.EditTextEmail;
        EditText password = binding.EditTextPassword;

        Log.d(TAG, "Informations passed");

        getUserInfo(email.getText().toString());

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user); TODO

                            Intent goTo = new Intent(SignInActivity.this, MainProfileActivity.class);
                            startActivity(goTo);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null); TODO
                            // ...
                        }

                        // ...
                    }
                });
    }

    private void getUserInfo(String email) {
        /*
        database.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "getUserInfo");
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "User Info Is " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
                */

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();

        binding = ActivitySigninBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);

        styleAllText();

        binding.ButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });
    }
}