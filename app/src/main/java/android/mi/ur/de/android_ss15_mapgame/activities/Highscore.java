package android.mi.ur.de.android_ss15_mapgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.mi.ur.de.android_ss15_mapgame.MainActivity;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.mi.ur.de.android_ss15_mapgame.persistence.LocalHighscoreDb;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Daniel on 24.08.2015.
 */
public class Highscore extends Activity{

    private Button menu;
    private TextView bestHighscore;
    private LocalHighscoreDb db;
    private String score;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDB();
        updateHighscore();
        setupUI();

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
    }

    private void initDB() {
        db = new LocalHighscoreDb(this);
        db.open();
    }

    private void updateHighscore() {
        score = String.valueOf(db.getScore());
    }

}
