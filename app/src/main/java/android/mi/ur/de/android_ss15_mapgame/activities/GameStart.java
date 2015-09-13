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
public class GameStart extends Activity {

    private static final int GERMANY = 0;
    private static final int EUROPE = 1;
    private static final int WORLD = 2;

    private Button germany;
    private Button europa;
    private Button worldwide;
    private Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUI();


        germany = (Button) findViewById(R.id.germany);
        germany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(GameStart.this, GameActivity.class);
                nextActivity.putExtra("region", GERMANY);
                startActivity(nextActivity);
            }
        });


        europa = (Button) findViewById(R.id.europa);
        europa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextActivity = new Intent(GameStart.this, GameActivity.class);
                nextActivity.putExtra("region", EUROPE);
                startActivity(nextActivity);

            }
        });


        worldwide = (Button) findViewById(R.id.worldwide);
        worldwide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextActivity = new Intent(GameStart.this, GameActivity.class);
                nextActivity.putExtra("region", WORLD);
                startActivity(nextActivity);

            }
        });


        menu = (Button) findViewById(R.id.gamestartMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(GameStart.this, MainActivity.class);
                startActivity(nextActivity);
            }
        });


    }

    private void setupUI(){
        setContentView(R.layout.game_start);


    }
}
