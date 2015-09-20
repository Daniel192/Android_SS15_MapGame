package android.mi.ur.de.android_ss15_mapgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.mi.ur.de.android_ss15_mapgame.MainActivity;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.mi.ur.de.android_ss15_mapgame.persistence.LocalHighscoreDb;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;

/**
 * Created by Daniel on 24.08.2015.
 */
public class GameResult extends Activity {
    private String score;
    private TextView scoreValue;
    private TextView highScore;
    private EditText name;

    private Button again;
    private Button menu;
    private Button submit;

    private LocalHighscoreDb db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDB();
        getExtras();
        updateHighscore();
        setupUI();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        score = extras.getString("score");
    }

    private void updateHighscore() {
        if(Integer.parseInt(score) > db.getScore()){
            db.updateScore(Integer.parseInt(score));
        }
    }

    private void initDB() {
        db = new LocalHighscoreDb(this);
        db.open();
    }

    private void setupUI(){
        setContentView(R.layout.game_result);

        scoreValue = (TextView) findViewById(R.id.scoreValue);
        scoreValue.setText(score);

        highScore = (TextView) findViewById(R.id.gameResultHighscore);
        highScore.setText(String.valueOf(db.getScore()));

        again = (Button) findViewById(R.id.playagain);
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(GameResult.this, GameStart.class);
                startActivity(nextActivity);
            }
        });

        menu = (Button) findViewById(R.id.resultMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(GameResult.this, MainActivity.class);
                startActivity(nextActivity);
            }
        });

        name = (EditText) findViewById(R.id.scoreName);

        submit = (Button) findViewById(R.id.submitHighscore);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseObject gameScore = new ParseObject("GameScore");
                gameScore.put("playerName", name.getText().toString());
                gameScore.put("score", score);
                gameScore.saveInBackground();
                submit.setEnabled(false);
            }
        });

    }
}
