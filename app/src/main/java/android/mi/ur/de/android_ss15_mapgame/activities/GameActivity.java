package android.mi.ur.de.android_ss15_mapgame.activities;

import android.content.Intent;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.mi.ur.de.android_ss15_mapgame.game.ScoreCalculator;
import android.mi.ur.de.android_ss15_mapgame.persistence.QuestionDb;
import android.mi.ur.de.android_ss15_mapgame.utility.QuestionItem;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;


public class GameActivity extends FragmentActivity implements OnMapReadyCallback{

    /* Steuert Nutzereingabe
     * Benachrichtigt GameController, wenn der Nutzer eine Antwort bestätigt
     * Aktualisiert Views, wenn der GameController eine neue Frage aus der DatenBank holt
     * Wechselt am Ende des Spiels zu GameResult und übergibt dabei den Score
     */

    private static final LatLng GERMANY = new LatLng(51.17,10.45);

    private GoogleMap quizMap;
    private UiSettings quizMapUiSettings;

    private QuestionDb questionDb;
    private ArrayList<QuestionItem> questionArray = new ArrayList<>();
    private QuestionItem currentQuestion;
    private int currentQuestionId = 0;

    private TextView questionView;
    private TextView scoreView;

    private Button confirmButton;
    private CountDownTimer timer;

    private ProgressBar progressBar;
    private int progressStatus = 0;

    private Marker guessMarker;
    private Marker targetMarker;
    private LatLng target;
    private LatLng guess;

    private double distance;
    private double score = 0;
    private int gameTimeMillis = 60000;
    private ScoreCalculator scoreCalculator = new ScoreCalculator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUI();
        initDb();
        loadQuestions();
        setupTimer();
        setupMap();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void initDb(){
        questionDb = new QuestionDb(this);
        questionDb.open();
    }

    private void loadQuestions(){
        questionArray = questionDb.getAllQuestionItems();
    }

    private void updateQuestion(){
        currentQuestion = questionArray.get(currentQuestionId);
        questionView.setText(currentQuestion.getQuestion());
        target = currentQuestion.getTargetLocation();
    }

    private void setupUI(){
        setContentView(R.layout.game_activity);
        questionView = (TextView) findViewById(R.id.exercise);
        scoreView = (TextView) findViewById(R.id.score);
        progressBar = (ProgressBar) findViewById(R.id.time);
        confirmButton = (Button) findViewById(R.id.confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guess != null && target != null) {
                    Log.d("TAG", "ConfirmGuess");
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
                //questionView.setText("seconds remaining: " + millisUntilFinished / 1000);
                progressStatus += 1;
                progressBar.setProgress(progressStatus);
            }

            public void onFinish() {
                stopGame();
            }
        };
    }

    private void startTimer(){
        progressBar.setMax(60);
        progressBar.setProgress(0);
        timer.start();
    }

    private void confirmGuess(){
        distance = SphericalUtil.computeDistanceBetween(guess, target);
        score += scoreCalculator.calculateScore(distance);
        scoreView.setText(String.valueOf(score));
        showTarget();
    }

    private void showTarget(){
        targetMarker = quizMap.addMarker(new MarkerOptions()
                .position(target)
                .title("Ziel"));
        targetMarker.showInfoWindow();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetMarker.remove();
                currentQuestionId++;
                if(currentQuestionId == questionArray.size()-1){
                    stopGame();
                }
                updateQuestion();
            }
        },3000);
    }

    private void stopGame(){
        Intent nextActivity = new Intent(GameActivity.this, GameResult.class);
        nextActivity.putExtra("score", score);
        startActivity(nextActivity);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        quizMap = map;
        quizMapUiSettings = quizMap.getUiSettings();
        quizMapUiSettings.setCompassEnabled(false);
        quizMapUiSettings.setRotateGesturesEnabled(false);
        quizMapUiSettings.setMapToolbarEnabled(false);
        quizMapUiSettings.setZoomControlsEnabled(false);

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.moveCamera(CameraUpdateFactory.newLatLng(GERMANY));

        setListenersOnMap();
    }

    private void setListenersOnMap(){
        quizMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.d("TAG", "onMapLoaded");
                updateQuestion();
                startTimer();
            }
        });

        quizMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                guess = latLng;
                if (guessMarker == null) {
                    guessMarker = quizMap.addMarker(new MarkerOptions()
                            .position(guess)
                            .draggable(true)
                            .title("Vermutung"));
                    guessMarker.showInfoWindow();
                } else {
                    guessMarker.setPosition(guess);
                }
            }
        });
    }

}

