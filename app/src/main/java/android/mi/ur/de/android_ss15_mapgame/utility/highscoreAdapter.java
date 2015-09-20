package android.mi.ur.de.android_ss15_mapgame.utility;

import android.content.Context;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 20.09.2015.
 */
public class highscoreAdapter extends ArrayAdapter<ParseObject>{

    private Context context;
    private List<ParseObject> itemList;

    public highscoreAdapter(Context context, List<ParseObject> listItems) {
        super(context, R.layout.score_item, listItems);

        this.context = context;
        this.itemList = listItems;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.score_item, null);
        }

        ParseObject item = itemList.get(position);

        if (item != null) {
            TextView name = (TextView) v.findViewById(R.id.onlinescoreName);
            TextView score = (TextView) v.findViewById(R.id.onlineScoreValue);

            name.setText(item.getString("playerName"));
            score.setText(item.getString("score"));
        }

        return v;
    }
}
