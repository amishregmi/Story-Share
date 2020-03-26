package aregmi.ramapo.edu.storyshare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private View current;
    private TextView summary_textview;
    private TextView textview_summary_tag;
    private TextView extracted_emotions_textview;
    private TextView emotions_textview;
    private TextView categories_extracted_textview;
    private Button save_button;
    private List<String> categories_extracted_arraylist;
    private String story_key;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        story_key = "";
        current = inflater.inflate(R.layout.fragment_home, container, false);
        Button upload_button = current.findViewById(R.id.upload_button);
        summary_textview = current.findViewById(R.id.summary_textview);
        textview_summary_tag = current.findViewById(R.id.textview_summary_tag);
        extracted_emotions_textview = current.findViewById(R.id.extracted_emotions_textview);
        emotions_textview = current.findViewById(R.id.emotions_textview);
        categories_extracted_textview = current.findViewById(R.id.categories_extracted_textview);
        save_button = current.findViewById(R.id.save_button);
        //categories_extracted_arraylist.clear();

        turnVisibilityOff();

        upload_button.setOnClickListener(this);
        save_button.setOnClickListener(this);

        return current;
    }

    private void turnVisibilityOff() {
        summary_textview.setVisibility(View.INVISIBLE);
        textview_summary_tag.setVisibility(View.INVISIBLE);
        extracted_emotions_textview.setVisibility(View.INVISIBLE);
        emotions_textview.setVisibility(View.INVISIBLE);
        categories_extracted_textview.setVisibility(View.INVISIBLE);
        save_button.setVisibility(View.INVISIBLE);
    }

    private void turnVisibilityOn(){
        summary_textview.setVisibility(View.VISIBLE);
        textview_summary_tag.setVisibility(View.VISIBLE);
        extracted_emotions_textview.setVisibility(View.VISIBLE);
        emotions_textview.setVisibility(View.VISIBLE);
        categories_extracted_textview.setVisibility(View.VISIBLE);
        summary_textview.setMovementMethod(new ScrollingMovementMethod());
        emotions_textview.setMovementMethod(new ScrollingMovementMethod());
        save_button.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.upload_button:
                uploadFile();
                break;

            case R.id.save_button:
                saveToExtractedCategories();
                break;
        }

    }

    private void saveToExtractedCategories() {
        System.out.println("Inside saveToExtractedCategories Function");
        for (String one_category : categories_extracted_arraylist){
            String category_without_quotes = one_category.replace("\"","");
            //DatabaseReference user_db_reference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
            DatabaseReference category = FirebaseDatabase.getInstance().getReference().child("Categories").child(category_without_quotes);
            System.out.println("ONE_CATEGORY IS: "+ one_category);
            category.child(story_key).setValue("True");
        }

        turnVisibilityOff();

    }

    private void uploadFile() {
        AlertDialog.Builder load_file = new AlertDialog.Builder(getActivity());
        //String selected_file = "";
        load_file.setTitle("Select Story");

        final String saved_stories_dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/saved_stories";
        System.out.println("SAVED STORIES DIR IS: "+ saved_stories_dir);

        final File[] all_files = new File(saved_stories_dir).listFiles();
        List<String> text_files = new ArrayList<String>();


            for (File onefile: all_files){
                String file_name = onefile.getName();
                if (file_name.endsWith(".txt")){
                    text_files.add(file_name);
                }
            }

            int size = text_files.size();
            String[] options = new String[size];
            options = text_files.toArray(options);

        AlertDialog.Builder builder = load_file.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView options = ((AlertDialog) dialog).getListView();
                String selected_file = (String) options.getAdapter().getItem(which);
                System.out.println("Selected file is: " + selected_file);
                String filename_withpath = saved_stories_dir + "/" + selected_file;

                String all_contents = readFileContents(filename_withpath);

                try {
                    sendDjangoRequest(all_contents);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        load_file.show();
        //}

    }

    private String readFileContents(String filename_withpath) {

        File file = new File(filename_withpath);
        StringBuilder fileContents = new StringBuilder((int)file.length());

        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()){
                fileContents.append(scanner.nextLine()+ System.lineSeparator());
            }

            return fileContents.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void sendDjangoRequest(final String all_contents) throws JSONException {


        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            String URL = "https://9f6349a5.ngrok.io/story_share/";
            //http://9f6349a5.ngrok.io
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
            jsonBody.put("story_body", all_contents);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getContext(), "200", Toast.LENGTH_LONG).show();
                    String summary = parseSummaryFromResponse(response);
                    String emotions_extracted = parseEmotionsFromResponse(response);
                    String categories_extracted = parseCategoriesFromResponse(response);

                    saveCategoriesExtracted(categories_extracted);

                    String display_text = "The story will be added to categories: "+ categories_extracted;
                    categories_extracted_textview.setText(display_text);

                    emotions_textview.setText(emotions_extracted.toString());
                    summary_textview.setText(summary);
                    turnVisibilityOn();
                    DatabaseReference db_stories_ref = FirebaseDatabase.getInstance().getReference().child("Stories").push();
                    story_key = db_stories_ref.getKey();
                    Map story_details = new HashMap();
                    String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    story_details.put("user_id", current_user_id);
                    story_details.put("whole_story", all_contents);
                    story_details.put("story_summary", summary);
                    story_details.put("emotional_scores", emotions_extracted);
                    story_details.put("categories_extracted", categories_extracted);
                    DatabaseReference this_story_ref = FirebaseDatabase.getInstance().getReference().child("Stories").child(story_key);
                    this_story_ref.updateChildren(story_details);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);

                    }
                    return Response.success(new String(response.data), HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
            //Toast.makeText(getContext(), stringRequest.toString(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void saveCategoriesExtracted(String categories_extracted) {
        String [] items = categories_extracted.split("\\s*,\\s*");
        categories_extracted_arraylist = Arrays.asList(items);
        System.out.println("Inside saveCategoriesExtracted : "+ categories_extracted_arraylist.toString());
    }

    private String parseCategoriesFromResponse(String response) {
        String categories = new String("categories_and_sentiments");
        int outer_categories = response.indexOf(categories);
        int inner_categories = response.indexOf("categories", outer_categories+10);
        int end_index = response.indexOf("]", inner_categories+5);
        String required = response.substring(inner_categories+14, end_index);
        System.out.println("ADDED TO CATEGORIES: ");
        System.out.println(required);

        return required;
    }

    private String parseEmotionsFromResponse(String response) {
        String computed_sentiments = new String("computed_sentiments");
        String ending_threequotes = new String("}}}");
        int index_start = response.indexOf(computed_sentiments);
        int end_index = response.indexOf(ending_threequotes, index_start+10);
        String required = response.substring(index_start+35, end_index);
        required = required.replace(",",",\n");
        System.out.println("Emotions parsed string is: ");
        System.out.println(required);

        return required;
    }

    private String parseSummaryFromResponse(String response) {

        String summary = new String("summary");
        int index_start = response.indexOf(summary);
        int end_index = response.indexOf("}",index_start+11);

        String required = response.substring(index_start+11, end_index);
        return required;
    }
}
