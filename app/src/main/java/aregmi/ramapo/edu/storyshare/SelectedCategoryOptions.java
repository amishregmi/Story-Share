package aregmi.ramapo.edu.storyshare;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectedCategoryOptions extends Activity implements AdapterView.OnItemClickListener {

    private List<String> titles ;
    private int total_num_stories;
    private int fetch_count;
    private List<String> descriptions;
    private List<String> story_ids ;
    private List<String> user_ids;
    private String selected_category = "";
    ListView listView;
    List<Single_Available_Story_Item> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category_options);

        selected_category = getIntent().getExtras().getString("selected_category");
        System.out.println("The selected category is after selection is: "+ selected_category);

        fetch_count = 0;
        story_ids = new ArrayList<String>();
        titles = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        user_ids = new ArrayList<String>();
        rowItems = new ArrayList<Single_Available_Story_Item>();

        extractStoryIds();

        listView = (ListView) findViewById(R.id.available_stories_listview);

    }

    private void extractStoryIds() {

        DatabaseReference category_ref = FirebaseDatabase.getInstance().getReference().child("Categories").child(selected_category);

        category_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() !=0 ){
                    Map<String, String> map = (Map<String, String>)dataSnapshot.getValue();

                    for (String key: map.keySet()){
                        story_ids.add(key);
                        //getStoryTitles(key);
                    }
                    total_num_stories = story_ids.size();
                    //System.out.println("TOTAL NUMBER OF STORIES IS: "+ story_ids.size());
                    try {
                        getStoryTitles();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getStoryTitles() throws InterruptedException {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Stories");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String story_id = snapshot.getKey().toString();
                    if (story_ids.contains(story_id)){
                        DatabaseReference one_story_ref = FirebaseDatabase.getInstance().getReference().child("Stories").child(story_id);

                        one_story_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshotz) {
                                if (dataSnapshotz.exists() && dataSnapshotz.getChildrenCount()!= 0){
                                    fetch_count++;

                                    Map<String, Object> map = (Map<String, Object>) dataSnapshotz.getValue();

                                    if (map.get("story_title") != null){
                                        titles.add(map.get("story_title").toString());
                                    }

                                    if (map.get("user_id")!= null){
                                        user_ids.add(map.get("user_id").toString());
                                    }

                                    if (fetch_count >= total_num_stories){
                                        System.out.println(titles.toString());
                                        System.out.println(user_ids.toString());
                                        populateListView();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void populateListView() {

        for (int i = 0; i < titles.size(); i++){
            String user_id = user_ids.get(i);
            String title = titles.get(i);
            Single_Available_Story_Item item = new Single_Available_Story_Item(title, "Author: "+user_id);
            rowItems.add(item);
        }

        Custom_ListView_StoryOptions_Adapter adapter = new Custom_ListView_StoryOptions_Adapter(SelectedCategoryOptions.this, R.layout.activity_single_available_story_display_, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(SelectedCategoryOptions.this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getApplicationContext(), "Position is: "+position,
                Toast.LENGTH_LONG).show();

        String story_id = story_ids.get(position);
        Intent intent = new Intent(SelectedCategoryOptions.this, SelectedStorySummary.class);
        intent.putExtra("story_id", story_id);
        startActivity(intent);
    }
}
