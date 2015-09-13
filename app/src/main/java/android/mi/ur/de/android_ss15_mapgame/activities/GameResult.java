package android.mi.ur.de.android_ss15_mapgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.mi.ur.de.android_ss15_mapgame.MainActivity;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Daniel on 24.08.2015.
 */
public class GameResult extends Activity {
    private String score;
    private TextView highscoreValue;

    private Button again;
    private Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        score = extras.getString("score");

        setupUI();

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
    }

    private void setupUI(){
        setContentView(R.layout.game_result);

        highscoreValue = (TextView) findViewById(R.id.scoreValue);
        highscoreValue.setText(score);

    }
}
