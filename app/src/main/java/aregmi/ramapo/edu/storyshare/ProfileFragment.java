package aregmi.ramapo.edu.storyshare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private View current;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        current = inflater.inflate(R.layout.activity_profile, container, false);
        Button logout_button = current.findViewById(R.id.logout_button);
        Button save_button = current.findViewById(R.id.button_save_profile);
        logout_button.setOnClickListener(this);
        save_button.setOnClickListener(this);

        return current;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.logout_button:
                String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            case R.id.button_save_profile:
                saveUserDetails();

        }
    }

    private void saveUserDetails() {
        EditText user_name = current.findViewById(R.id.user_name);
        String user_name_extract = user_name.getText().toString();
        EditText user_description = current.findViewById(R.id.user_description);
        String user_description_extract = user_description.getText().toString();

        System.out.println("USER NAME IS: "+ user_name_extract);
        System.out.println("USER DESCRIPTION IS: "+ user_description_extract);

        //DATABASE STUFF NOW

        FirebaseAuth firebase_db_auth = FirebaseAuth.getInstance();
        String current_user_id = firebase_db_auth.getCurrentUser().getUid();
        DatabaseReference user_db_reference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);

        Map user_details_save = new HashMap();
        user_details_save.put("user_name", user_name_extract);
        user_details_save.put("user_description", user_description_extract);
        user_db_reference.updateChildren(user_details_save);

    }

}
