package aregmi.ramapo.edu.storyshare;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomCategoriesListView extends ArrayAdapter<String>{

    private String[] category_names;
    private Integer[] img_ids;
    private Activity context;

    public CustomCategoriesListView(Activity context, String[] category_names, Integer[] img_ids) {
        super(context, R.layout.listview_categories, category_names);

        this.context = context;
        this.category_names = category_names;
        this.img_ids = img_ids;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null){

            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listview_categories, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.image_view.setImageResource(img_ids[position]);
        viewHolder.text_view.setText(category_names[position]);

        return r;
    }

    class ViewHolder {

        TextView text_view;
        ImageView image_view;

        ViewHolder(View v){
            text_view = v.findViewById(R.id.categories_textView);
            image_view = v.findViewById(R.id.categories_imageView);

        }

    }

}
