package aregmi.ramapo.edu.storyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Map;

public class SelectedStorySummary extends AppCompatActivity {

    private TextView summary_textvieww;
    private TextView emotions_textvieww;
    private TextView belonging_categories;
    private Button read_story_button;
    private TextView story_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_story_summary);

        summary_textvieww = findViewById(R.id.summary_textvieww);
        emotions_textvieww = findViewById(R.id.emotions_textvieww);
        belonging_categories = findViewById(R.id.belonging_categories);
        read_story_button = findViewById(R.id.read_story_button);
        story_title = findViewById(R.id.story_title);

        summary_textvieww.setMovementMethod(new ScrollingMovementMethod());
        emotions_textvieww.setMovementMethod(new ScrollingMovementMethod());


        final String story_id = getIntent().getExtras().getString("story_id");
        extractStoryDetails(story_id);

        read_story_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedStorySummary.this, FullStoryActivity.class);
                intent.putExtra("story_id", story_id);
                startActivity(intent);

            }
        });

    }

    private void extractStoryDetails(String story_id) {

        DatabaseReference story_ref = FirebaseDatabase.getInstance().getReference().child("Stories").child(story_id);

        story_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() != 0){

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("story_summary") != null){
                        String summary = map.get("story_summary").toString();
                        summary_textvieww.setText(summary);
                    }

                    if (map.get("emotional_scores") != null){
                        String emotional_scores = map.get("emotional_scores").toString();
                        emotions_textvieww.setText(emotional_scores);
                    }

                    if (map.get("categories_extracted") != null){
                        String categories_extracted = map.get("categories_extracted").toString();
                        belonging_categories.setText("Categories: "+categories_extracted);
                    }

                    if (map.get("story_title") != null){
                        String title = map.get("story_title").toString();
                        story_title.setText("Title: "+title);
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
