package aregmi.ramapo.edu.storyshare;

import android.app.Activity;
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

    private List<String> authors = new ArrayList<>();
    private List<String> summarys = new ArrayList<>();

    ListView listView;
    List<SingleAvailableStoryOption> rowItems;
    private List<String> matching_story_ids = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category_options);

        rowItems = new ArrayList<SingleAvailableStoryOption>();
        String required_category = getIntent().getExtras().getString("selected_category");
        System.out.println("Extracted required_category is: "+ required_category);
        DatabaseReference story_ids = FirebaseDatabase.getInstance().getReference().child("Categories").child(required_category);

        story_ids.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println("INSIDE ONDATACHANGE");
                if (dataSnapshot.exists()){
                    //System.out.println("INSIDE DATASNAPSHOT EXISTS");

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        //System.out.println("VALUE FOUND");
                        matching_story_ids.add(ds.getKey());
                    }

                    System.out.println("MATCHING STORY IDS: "+ matching_story_ids.toString());
                    //getStorySummaryDetails();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getStorySummaryDetails();
    }

    private void getStorySummaryDetails() {
        String current_story_id;
        //String current_author_id;

        for (int i = 0; i < matching_story_ids.size(); i++){

            current_story_id = matching_story_ids.get(i);
            DatabaseReference story_details = FirebaseDatabase.getInstance().getReference().child("Stories").child(current_story_id);


            story_details.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()!=0){
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                        if (map.get("story_summary") != null){
                            System.out.println("story_summary extracted");
                            summarys.add(map.get("story_summary").toString());
                        }

                        if (map.get("user_id") != null){
                            String current_user_id = map.get("user_id").toString();

                            DatabaseReference for_user_details = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);

                            for_user_details.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()!=0){

                                        System.out.println("INSIDE USER DETAILS");

                                        Map<String, Object> m_map = (Map<String, Object>) dataSnapshot.getValue();

                                        if (m_map.get("user_name")!= null){
                                            String retrieved_user_name = m_map.get("user_name").toString();
                                            authors.add(retrieved_user_name);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }

                    for (int j = 0 ; j < summarys.size(); j++){
                        SingleAvailableStoryOption item = new SingleAvailableStoryOption("Author: "+authors.get(j), "Summary: "+summarys.get(j));
                        rowItems.add(item);
                    }

                    System.out.println("ROW ITEMS ARE: "+ rowItems);

                    View rootview = findViewById(android.R.id.content).getRootView();

                    listView = (ListView) rootview.findViewById(R.id.selectedcatoptions_listview);
                    CustomSelectedCategoryListView adapter = new CustomSelectedCategoryListView(getApplicationContext(), R.layout.listview_selectedcategory, rowItems);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(SelectedCategoryOptions.this);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), position, Toast.LENGTH_LONG).show();
    }
}
