package android.mi.ur.de.android_ss15_mapgame.activities;

import android.app.Activity;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Daniel on 24.08.2015.
 */
public class GameResult extends Activity {
    private String score;
    private TextView highscoreValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        score = extras.getString("score");

        setupUI();
    }

    private void setupUI(){
        setContentView(R.layout.game_result);

        highscoreValue = (TextView) findViewById(R.id.highscoreValue);
        highscoreValue.setText(score);

    }
}
