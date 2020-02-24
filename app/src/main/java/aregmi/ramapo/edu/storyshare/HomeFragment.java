package aregmi.ramapo.edu.storyshare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private View current;
    private TextView summary_textview;
    private TextView textview_summary_tag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        current = inflater.inflate(R.layout.fragment_home, container, false);
        Button upload_button = current.findViewById(R.id.upload_button);
        summary_textview = current.findViewById(R.id.summary_textview);
        summary_textview.setVisibility(View.INVISIBLE);
        textview_summary_tag = current.findViewById(R.id.textview_summary_tag);
        textview_summary_tag.setVisibility(View.INVISIBLE);

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

            String URL = "https://44cd5543.ngrok.io/story_share/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
            jsonBody.put("story_body", all_contents);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    String summary = parseSummaryFromResponse(response);
                    summary_textview.setText(summary);
                    summary_textview.setVisibility(View.VISIBLE);
                    summary_textview.setMovementMethod(new ScrollingMovementMethod());
                    textview_summary_tag.setVisibility(View.VISIBLE);
                    DatabaseReference db_stories_ref = FirebaseDatabase.getInstance().getReference().child("Stories").push();
                    String story_key = db_stories_ref.getKey();
                    Map story_details = new HashMap();
                    String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    story_details.put("user_id", current_user_id);
                    story_details.put("whole_story", all_contents);
                    story_details.put("story_summary", response.toString());
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

    private String parseSummaryFromResponse(String response) {

        String summary = new String("summary");
        int index_start = response.indexOf(summary);
        int end_index = response.indexOf('"',index_start+11);

        String required = response.substring(index_start+11, end_index);
        //System.out.println("PARSED SUMMARY IS: ");
        //System.out.println(required);

        return required;
    }
}
