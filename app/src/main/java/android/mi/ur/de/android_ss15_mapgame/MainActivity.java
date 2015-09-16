package android.mi.ur.de.android_ss15_mapgame;

import android.content.Intent;
import android.mi.ur.de.android_ss15_mapgame.activities.GameActivity;
import android.mi.ur.de.android_ss15_mapgame.activities.GameStart;
import android.mi.ur.de.android_ss15_mapgame.activities.Highscore;
import android.mi.ur.de.android_ss15_mapgame.activities.Info;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    private Button singleplayerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        singleplayerButton = (Button) findViewById(R.id.buttonSingleplayer);
        singleplayerButton.setOnClickListener(new View.OnClickListener() {
             @Override
              public void onClick(View v) {

                 Intent nextActivity = new Intent(MainActivity.this, GameStart.class);
                 startActivity(nextActivity);

                  }
              });


       Button buttonHighscore = (Button) findViewById(R.id.buttonHighscore);
       buttonHighscore.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent nextActivity = new Intent(MainActivity.this, Highscore.class);
               startActivity(nextActivity);

           }
       });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        Intent nextActivity = new Intent(MainActivity.this, Info.class);
        startActivity(nextActivity);

        return super.onOptionsItemSelected(item);
    }
 }

