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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private View current;
    private EditText user_name;
    private EditText user_description;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        current = inflater.inflate(R.layout.activity_profile, container, false);
        user_name = current.findViewById(R.id.user_name);
        user_description = current.findViewById(R.id.user_description);
        getCurrentProfileDetails();
        Button logout_button = current.findViewById(R.id.logout_button);
        Button save_button = current.findViewById(R.id.button_save_profile);
        logout_button.setOnClickListener(this);
        save_button.setOnClickListener(this);

        return current;
    }

    private void getCurrentProfileDetails() {
        FirebaseAuth firebase_db_auth = FirebaseAuth.getInstance();
        String current_user_id = firebase_db_auth.getCurrentUser().getUid();
        DatabaseReference user_db_reference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);

        user_db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Check if data already exists

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() !=0 ){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("user_name") != null){
                        String current_name = map.get("user_name").toString();
                        user_name.setText(current_name);
                    }

                    if (map.get("user_description") != null){
                        String current_description = map.get("user_description").toString();
                        user_description.setText(current_description);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        String user_name_extract = user_name.getText().toString();
        String user_description_extract = user_description.getText().toString();

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
