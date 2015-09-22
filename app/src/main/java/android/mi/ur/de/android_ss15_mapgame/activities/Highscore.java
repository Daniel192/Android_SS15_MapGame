package android.mi.ur.de.android_ss15_mapgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.mi.ur.de.android_ss15_mapgame.MainActivity;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.mi.ur.de.android_ss15_mapgame.persistence.LocalHighscoreDb;
import android.mi.ur.de.android_ss15_mapgame.utility.highscoreAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Daniel on 24.08.2015.
 */
public class Highscore extends Activity{

    private Button menu;
    private TextView bestHighscore;
    private ListView bestestHighscores;
    private LocalHighscoreDb db;
    private String score;
    private highscoreAdapter itemAdapter;
    private List<ParseObject> itemList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDB();
        getLocalHighscore();
        setupUI();
        getScoreList();
    }

    private void setupUI(){
        setContentView(R.layout.highscore);

        menu = (Button) findViewById(R.id.highscoreMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(Highscore.this, MainActivity.class);
                startActivity(nextActivity);
            }
        });

        bestHighscore = (TextView) findViewById(R.id.bestHighscore);
        bestHighscore.setText(score);

        bestestHighscores = (ListView) findViewById(R.id.onlineScores);
    }

    private void initAdapter() {
        itemAdapter = new highscoreAdapter(this, itemList);
        bestestHighscores.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
    }

    private void getScoreList() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("GameScore");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Collections.sort(parseObjects, new Comparator<ParseObject>() {
                    @Override
                    public int compare(ParseObject objectOne, ParseObject objectTwo) {
                        if(Integer.parseInt(objectOne.getString("score"))>Integer.parseInt(objectTwo.getString("score"))){
                            return -1;
                        } else if(Integer.parseInt(objectOne.getString("score"))<Integer.parseInt(objectTwo.getString("score"))){
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                itemList = parseObjects;
                initAdapter();
            }
        });

    }

    private void initDB() {
        db = new LocalHighscoreDb(this);
        db.open();
    }

    private void getLocalHighscore() {
        score = "Dein Highscore: " + String.valueOf(db.getScore());
    }

}
