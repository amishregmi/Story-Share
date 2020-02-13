package aregmi.ramapo.edu.storyshare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private View current;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        current = inflater.inflate(R.layout.fragment_home, container, false);
        Button upload_button = current.findViewById(R.id.upload_button);

        upload_button.setOnClickListener(this);

        return current;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.upload_button:
                uploadFile();
        }

    }

    private void uploadFile() {
        AlertDialog.Builder load_file = new AlertDialog.Builder(getActivity());
        load_file.setTitle("Select Story");

        String saved_stories_dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/saved_stories";
        System.out.println("SAVED STORIES DIR IS: "+ saved_stories_dir);

        final File[] all_files = new File(saved_stories_dir).listFiles();
        List<String> text_files = new ArrayList<String>();

//        if (!(all_files.length == 0)){

            for (File onefile: all_files){
                String file_name = onefile.getName();
                if (file_name.endsWith(".txt")){
                    text_files.add(file_name);
                }
            }

            int size = text_files.size();
            String[] options = new String[size];
            options = text_files.toArray(options);

            load_file.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ListView options = ((AlertDialog)dialog).getListView();
                    String selected_file = (String) options.getAdapter().getItem(which);
                    System.out.println("Selected file is: "+ selected_file);

                }
            });

            load_file.show();
        //}


    }
}
