package android.mi.ur.de.android_ss15_mapgame.activities;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Collections;


public class GameActivity extends FragmentActivity implements OnMapReadyCallback{

    /* Steuert Nutzereingabe
     * Benachrichtigt GameController, wenn der Nutzer eine Antwort bestätigt
     * Aktualisiert Views, wenn der GameController eine neue Frage aus der DatenBank holt
     * Wechselt am Ende des Spiels zu GameResult und übergibt dabei den Score
     */

    private static final LatLng LAT_LNG_GERMANY = new LatLng(51.17,10.45);
    private static final LatLng LAT_LNG_EUROPE = new LatLng(49.5,22);
    private static final LatLng LAT_LNG_WORLD = new LatLng(28.9380657,3.1182884);
    private static final float ZOOM_GERMANY = 4.5f;
    private static final float ZOOM_EUROPE = 2.5f;
    private static final float ZOOM_WORLD = 1.0f;
    private static final int ON_TARGET_RADIUS_GERMANY = 25000;
    private static final int VERY_FAR_AWAY_RADIUS_GERMANY = 150000;
    private static final int ON_TARGET_RADIUS_EUROPE = 50000;
    private static final int VERY_FAR_AWAY_RADIUS_EUROPE = 300000;
    private static final int ON_TARGET_RADIUS_WORLD = 75000;
    private static final int VERY_FAR_AWAY_RADIUS_WORLD = 450000;


    private GoogleMap quizMap;
    private UiSettings quizMapUiSettings;
    private int region;

    private QuestionDb questionDb;
    private ArrayList<QuestionItem> questionArray = new ArrayList<>();
    private QuestionItem currentQuestion;
    private int currentQuestionId = 0;

    private TextView questionView;
    private TextView scoreView;
    private TextView timeView;

    private Button confirmButton;
    private Button startGame;
    private CountDownTimer timer;

    private ProgressBar progressBar;
    private int progressStatus = 0;

    private Marker guessMarker;
    private Marker targetMarker;
    private LatLng target;
    private LatLng guess;

    private Circle veryFarAwayCircle;
    private Circle onTargetCircle;
    private int veryFarAwayRadius;
    private int onTargetRadius;

    private double distance;
    private int score = 0;
    private int gameTimeMillis = 90000;
    private ScoreCalculator scoreCalculator = new ScoreCalculator();
    private LatLng currentRegion;
    private float currentZoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getExtras();
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
        finish();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        region = extras.getInt("region");

        switch (region){
            case 0:
                currentRegion = LAT_LNG_GERMANY;
                currentZoom = ZOOM_GERMANY;
                veryFarAwayRadius = VERY_FAR_AWAY_RADIUS_GERMANY;
                onTargetRadius = ON_TARGET_RADIUS_GERMANY;
                break;
            case 1:
                currentRegion = LAT_LNG_EUROPE;
                currentZoom = ZOOM_EUROPE;
                veryFarAwayRadius = VERY_FAR_AWAY_RADIUS_EUROPE;
                onTargetRadius = ON_TARGET_RADIUS_EUROPE;
                break;
            case 2:
                currentRegion = LAT_LNG_WORLD;
                currentZoom = ZOOM_WORLD;
                veryFarAwayRadius = VERY_FAR_AWAY_RADIUS_WORLD;
                onTargetRadius = ON_TARGET_RADIUS_WORLD;
                break;
        }
    }

    private void initDb(){
        questionDb = new QuestionDb(this);
        questionDb.open();
    }

    private void loadQuestions(){
        questionArray = questionDb.getAllQuestionItems(region);
        Collections.shuffle(questionArray);
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
        timeView = (TextView) findViewById(R.id.timeNumber);
        progressBar = (ProgressBar) findViewById(R.id.time);
        confirmButton = (Button) findViewById(R.id.confirm);
        startGame = (Button) findViewById(R.id.startGame);
        startGame.setEnabled(false);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guess != null && target != null) {
                    Log.d("TAG", "ConfirmGuess");
                    confirmGuess();
                }
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
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
                timeView.setText("Zeit: " + millisUntilFinished / 1000);
                progressStatus += 1;
                progressBar.setProgress(progressStatus);
            }

            public void onFinish() {
                stopGame();
            }
        };
    }

    private void startTimer(){
        progressBar.setMax(90);
        progressBar.setProgress(0);
        timer.start();
    }

    private void startGame(){
        startGame.setVisibility(View.GONE);
        updateQuestion();
        startTimer();
    }

    private void confirmGuess(){
        confirmButton.setEnabled(false);
        distance = SphericalUtil.computeDistanceBetween(guess, target);
        score += (int) scoreCalculator.calculateScore(distance, region);
        scoreView.setText(String.valueOf("Score: " + score));
        quizMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, currentZoom));
        showTarget();
    }

    private void showTarget(){
        targetMarker = quizMap.addMarker(new MarkerOptions()
                .position(target)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_flag))
                .anchor(0.3f, 0.82f)
                .title(currentQuestion.getAnswer())
                .snippet("Entfernung " + String.valueOf((int) (distance / 1000)) + " km"));
        targetMarker.showInfoWindow();

        veryFarAwayCircle = quizMap.addCircle(new CircleOptions()
                .center(targetMarker.getPosition())
                .radius(veryFarAwayRadius)
                .strokeWidth(3)
                .zIndex(0.0f));
        onTargetCircle = quizMap.addCircle(new CircleOptions()
                .center(targetMarker.getPosition())
                .radius(onTargetRadius)
                .fillColor(Color.BLACK)
                .zIndex(5.0f));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetMarker.remove();
                onTargetCircle.remove();
                veryFarAwayCircle.remove();
                currentQuestionId++;

                if (currentQuestionId == questionArray.size() - 1) {
                    stopGame();
                }
                quizMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentRegion, currentZoom));
                updateQuestion();
                confirmButton.setEnabled(true);
            }
        }, 3000);
    }

    private void stopGame(){
        Intent nextActivity = new Intent(GameActivity.this, GameResult.class);

        nextActivity.putExtra("score", String.valueOf(score));
        nextActivity.putExtra("region", region);
        startActivity(nextActivity);
        quizMap.moveCamera(CameraUpdateFactory.newLatLng(guess));
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

        quizMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        quizMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentRegion, currentZoom));

        setListenersOnMap();
    }

    private void updateMarker(LatLng latLng){
        guess = latLng;
        if (guessMarker == null) {
            guessMarker = quizMap.addMarker(new MarkerOptions()
                    .position(guess)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_pin))
                    .anchor(0.18f, 0.8f)
                    .draggable(true));
        } else {
            guessMarker.setPosition(guess);
        }

        /*
        if (guessCircle == null) {
            guessCircle = quizMap.addCircle(new CircleOptions()
                    .center(guess)
                    .radius(7500)
                    .strokeColor(Color.BLACK)
                    .fillColor(Color.RED)
                    .zIndex(6.0f));
        } else {
            guessCircle.setCenter(guess);
        }
        */
    }

    private void setListenersOnMap(){
        quizMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                startGame.setEnabled(true);
            }
        });

        quizMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                updateMarker(latLng);
            }
        });

        quizMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
             public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                guess = guessMarker.getPosition();
            }
        });
    }

}

