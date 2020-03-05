package aregmi.ramapo.edu.storyshare;

import android.content.Intent;
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
    //Bored, Angry, Sad, Fear, Happy, Excited
    //private String[] category_names = {"Bored", "History", "Action", "Humor", "Romance", "Happy", "Sad", "Fear", "Anger"};
    private String[] category_names = {"Sad", "Happy", "Excited", "Fear", "Angry", "Bored"};
    private Integer[] img_ids = {R.drawable.sad_icon, R.drawable.happy_icon, R.drawable.action_icon, R.drawable.fear_icon, R.drawable.anger_icon, R.drawable.drama_icon};


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
                String selected_category = getCategoryString(position);

                Intent intent = new Intent(getActivity(), SelectedCategoryOptions.class);
                intent.putExtra("selected_category", selected_category);
                startActivity(intent);

            }
        });

        return current;
    }

    private String getCategoryString(int position) {
        String required_string = "";
        if (position == 0){
            required_string = "Sad";
        }

        else if (position == 1){
            required_string = "Happy";
        }

        else if (position == 2){
            required_string = "Excited";
        }

        else if (position == 3){
            required_string = "Fear";
        }

        else if (position == 4){
            required_string = "Angry";
        }

        else{
            required_string = "Bored";
        }

        return required_string;
    }
}
