package gardenary.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;

import gardenary.app.databinding.ActivityMainprofileBinding;
import gardenary.app.databinding.ActivitySignupBinding;

public class MainProfileActivity extends AppCompatActivity {
    private static final String TAG = "MainProfileActivity";

    private ActivityMainprofileBinding binding;
    //private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainprofileBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        HashMap data = (HashMap)intent.getExtras().getSerializable("user");

        Log.d(TAG, "data collection:" + data);
        Log.d(TAG, "type " + data.get("firstname"));
        saveUserData(data);

        binding.headerHelloUser.setText("Hello\n" + data.get("firstname"));
    }

    public void saveUserData(HashMap userData){
        //TODO
    }

}