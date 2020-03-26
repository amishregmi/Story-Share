package aregmi.ramapo.edu.storyshare;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Custom_ListView_StoryOptions_Adapter extends ArrayAdapter<Single_Available_Story_Item>{

    Context context;

    public Custom_ListView_StoryOptions_Adapter(Context context, int resourceId, List<Single_Available_Story_Item> items){
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        //  ImageView host_img;
        TextView author_title;
        TextView author_description;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        Single_Available_Story_Item rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){

            convertView = mInflater.inflate(R.layout.activity_single_available_story_display_, null);
            holder = new ViewHolder();
            holder.author_title = (TextView)convertView.findViewById(R.id.author_name);
            holder.author_description = (TextView)convertView.findViewById(R.id.author_description);

            convertView.setTag(holder);

        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.author_description.setText(rowItem.getDescription());
        holder.author_title.setText(rowItem.getName());

        return convertView;
    }
}
