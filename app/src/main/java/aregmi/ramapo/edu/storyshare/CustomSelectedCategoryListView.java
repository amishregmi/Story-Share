package aregmi.ramapo.edu.storyshare;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CustomSelectedCategoryListView extends ArrayAdapter<SingleAvailableStoryOption>{
    Context context;

    public CustomSelectedCategoryListView(Context context, int resourceId, List<SingleAvailableStoryOption> items){
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder{
        TextView author_name;
        TextView story_summary;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder = null;
        SingleAvailableStoryOption rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){

            convertView = mInflater.inflate(R.layout.listview_selectedcategory, null);
            holder = new ViewHolder();
            holder.author_name = (TextView) convertView.findViewById(R.id.author_name);
            holder.story_summary = (TextView) convertView.findViewById(R.id.story_summary);

            convertView.setTag(holder);
        }

        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.author_name.setText(rowItem.getAuthorName());
        holder.story_summary.setText(rowItem.getStorySummary());

        return convertView;


    }

}
