package android.mi.ur.de.android_ss15_mapgame.activities;

import android.content.Intent;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.mi.ur.de.android_ss15_mapgame.game.ScoreCalculator;
import android.mi.ur.de.android_ss15_mapgame.persistence.QuestionDb;
import android.mi.ur.de.android_ss15_mapgame.utility.QuestionItem;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;



public class GameActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    /* Steuert Nutzereingabe
     * Benachrichtigt GameController, wenn der Nutzer eine Antwort bestätigt
     * Aktualisiert Views, wenn der GameController eine neue Frage aus der DatenBank holt
     * Wechselt am Ende des Spiels zu GameResult und übergibt dabei den Score
     */

    private GoogleMap map;

    private QuestionDb questionDb;
    private ArrayList <QuestionItem> questionArray = new ArrayList<>();
    private QuestionItem currentQuestion;

    private TextView questionView;
    private TextView scoreView;

    private Button confirmButton;
    private CountDownTimer timer;

    private ProgressBar progressBar;
    private int progressStatus = 0;

    private LatLng target;
    private LatLng guess;

    private double distance;
    private double score = 0;
    private int gameTimeMillis = 20000;
    private ScoreCalculator scoreCalculator = new ScoreCalculator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUI();
        initDb();
        loadQuestions();
        updateQuestion();
        setupTimer();
        setupMap();
    }

    private void initDb(){
        questionDb = new QuestionDb(this);
        questionDb.open();
    }

    private void loadQuestions(){
        for(int i = 1; i < 6; i++){
            questionArray.add(questionDb.getQuestionItem(i));
        }
    }

    private void updateQuestion(){
        if(!questionArray.isEmpty()){
            currentQuestion = questionArray.get(questionArray.size()-1);
            questionArray.remove(currentQuestion);
            questionView.setText(currentQuestion.getQuestion());
            target = currentQuestion.getTargetLocation();
        }
        //stopGame();
    }

    private void setupUI(){
        setContentView(R.layout.game_activity);
        questionView = (TextView) findViewById(R.id.exercise);
        progressBar = (ProgressBar) findViewById(R.id.time);
        confirmButton = (Button) findViewById(R.id.confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(guess != null && target != null){
                    Log.d("TAG","ConfirmGuess");
                    confirmGuess();
                }
            }
        });
    }

    private void setupMap(){
        SupportMapFragment map =
            (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gameMap);
        map.getMapAsync(this);

    }

    private void setupTimer(){
        timer = new CountDownTimer(gameTimeMillis, 1000) {

            public void onTick(long millisUntilFinished) {
                questionView.setText("seconds remaining: " + millisUntilFinished / 1000);
                progressStatus += 1;
                progressBar.setProgress(progressStatus);
            }

            public void onFinish() {
                stopGame();
                questionView.setText("done!");
            }
        };
    }

    private void startTimer(){
        progressBar.setMax(20);
        progressBar.setProgress(0);
        timer.start();
    }

    private void confirmGuess(){
        distance = SphericalUtil.computeDistanceBetween(guess, target);
        score += scoreCalculator.calculateScore(distance);
        scoreView.setText(String.valueOf(score));
        updateQuestion();

    }

    private void stopGame(){
        Intent nextActivity = new Intent(GameActivity.this, GameResult.class);
        nextActivity.putExtra("Score", score);
        startActivity(nextActivity);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng germany = new LatLng(51.17,10.45);

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.moveCamera(CameraUpdateFactory.newLatLng(germany));

        startTimer();
    }



    @Override
    public void onMapClick(LatLng latLng) {
        guess = latLng;
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Vermutung"));

    }
}

