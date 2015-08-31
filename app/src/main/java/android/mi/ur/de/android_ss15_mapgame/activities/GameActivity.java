package android.mi.ur.de.android_ss15_mapgame.activities;

import android.content.Intent;
import android.mi.ur.de.android_ss15_mapgame.MapActivity;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class GameActivity extends FragmentActivity implements OnMapReadyCallback {

    /* Steuert Nutzereingabe
     * Benachrichtigt GameController, wenn der Nutzer eine Antwort bestätigt
     * Aktualisiert Views, wenn der GameController eine neue Frage aus der DatenBank holt
     * Wechselt am Ende des Spiels zu GameResult und übergibt dabei den Score
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gameMap);
        mapFragment.getMapAsync(this);
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}

