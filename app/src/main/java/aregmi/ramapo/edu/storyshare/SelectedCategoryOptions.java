package aregmi.ramapo.edu.storyshare;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectedCategoryOptions extends Activity implements AdapterView.OnItemClickListener {

    private List<String> titles = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();

    ListView listView;
    List<Single_Available_Story_Item> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category_options);

        rowItems = new ArrayList<Single_Available_Story_Item>();

        Single_Available_Story_Item item = new Single_Available_Story_Item("ABC", "ONEONASDASD");
        rowItems.add(item);

        item = new Single_Available_Story_Item("ASDASDA", "ASDASDASDASD");
        rowItems.add(item);

        listView = (ListView) findViewById(R.id.available_stories_listview);
        Custom_ListView_StoryOptions_Adapter adapter = new Custom_ListView_StoryOptions_Adapter(SelectedCategoryOptions.this, R.layout.activity_single_available_story_display_, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(SelectedCategoryOptions.this);


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "ANC",
                Toast.LENGTH_LONG).show();
    }
}
