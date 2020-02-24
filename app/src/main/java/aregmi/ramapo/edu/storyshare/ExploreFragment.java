package aregmi.ramapo.edu.storyshare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ExploreFragment extends Fragment{

    private ListView caterogires_listview;
    private View current;
    private String[] category_names = {"Drama", "History", "Action", "Humor", "Romance", "Happy", "Sad", "Fear", "Anger"};
    private Integer[] img_ids = {R.drawable.drama_icon, R.drawable.history_icon, R.drawable.action_icon, R.drawable.humor_icon,
    R.drawable.romance_icon, R.drawable.happy_icon, R.drawable.sad_icon, R.drawable.fear_icon, R.drawable.anger_icon};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        current = inflater.inflate(R.layout.fragment_explore, container, false);
        caterogires_listview = current.findViewById(R.id.categories_listview);
        CustomCategoriesListView customCategoriesListView = new CustomCategoriesListView(getActivity(), category_names, img_ids);
        caterogires_listview.setAdapter(customCategoriesListView);

        caterogires_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String toast_msg = "Position: "+position+" id: "+id;
                Toast.makeText(getActivity(), toast_msg, Toast.LENGTH_LONG).show();
            }
        });

        return current;
    }
}
