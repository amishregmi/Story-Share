package aregmi.ramapo.edu.storyshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class FullStoryActivity extends AppCompatActivity {

    private TextView full_story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_story);

        full_story = findViewById(R.id.full_story);
        full_story.setMovementMethod(new ScrollingMovementMethod());
        String story_id = getIntent().getStringExtra("story_id");

        DatabaseReference story_ref = FirebaseDatabase.getInstance().getReference().child("Stories").child(story_id);

        story_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()!=0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if(map.get("whole_story")!= null){
                        String whole_story = map.get("whole_story").toString();
                        full_story.setText(whole_story);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
