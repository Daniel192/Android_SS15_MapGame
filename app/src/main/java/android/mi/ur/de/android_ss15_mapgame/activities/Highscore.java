package android.mi.ur.de.android_ss15_mapgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.mi.ur.de.android_ss15_mapgame.MainActivity;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.mi.ur.de.android_ss15_mapgame.persistence.LocalHighscoreDb;
import android.mi.ur.de.android_ss15_mapgame.utility.highscoreAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Daniel on 24.08.2015.
 */
public class Highscore extends Activity {

    private Button menu;
    private TextView localHighscore;
    private ListView highscoreListView;
    private LocalHighscoreDb db;
    private String score;
    private highscoreAdapter itemAdapter;
    private List<ParseObject> itemList;
    private Spinner spinner;

    private static int GERMANY = 0;
    private static int EUROPE = 1;
    private static int WORLD = 2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDB();
        setupUI();
        getLocalHighscore(GERMANY);
        getScoreList(GERMANY);
    }

    private void setupUI() {
        setContentView(R.layout.highscore);

        menu = (Button) findViewById(R.id.highscoreMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(Highscore.this, MainActivity.class);
                startActivity(nextActivity);
            }
        });

        localHighscore = (TextView) findViewById(R.id.bestHighscore);

        highscoreListView = (ListView) findViewById(R.id.onlineScores);

        setupSpinner();
    }

    private void initAdapter() {
        itemAdapter = new highscoreAdapter(this, itemList);
        highscoreListView.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
    }

    private void getScoreList(int region) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("GameScore");
        query.whereEqualTo("region", region);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Collections.sort(parseObjects, new Comparator<ParseObject>() {
                    @Override
                    public int compare(ParseObject objectOne, ParseObject objectTwo) {
                        if (Integer.parseInt(objectOne.getString("score")) > Integer.parseInt(objectTwo.getString("score"))) {
                            return -1;
                        } else if (Integer.parseInt(objectOne.getString("score")) < Integer.parseInt(objectTwo.getString("score"))) {
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

    private void getLocalHighscore(int region) {
        score = "Dein Highscore: " + String.valueOf(db.getScore(region));
        localHighscore.setText(score);
    }


    private void setupSpinner() {

        spinner = (Spinner) findViewById(R.id.chooseCountry);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        getScoreList(GERMANY);
                        getLocalHighscore(GERMANY);
                        break;

                    case 1:
                        getScoreList(EUROPE);
                        getLocalHighscore(EUROPE);
                        break;

                    case 2:
                        getScoreList(WORLD);
                        getLocalHighscore(WORLD);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}