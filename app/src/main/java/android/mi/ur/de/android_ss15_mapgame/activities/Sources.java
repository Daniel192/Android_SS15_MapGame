package android.mi.ur.de.android_ss15_mapgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.mi.ur.de.android_ss15_mapgame.MainActivity;
import android.mi.ur.de.android_ss15_mapgame.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Daniel on 24.08.2015.
 */
public class Sources extends Activity {

    Button sourcesMenu;
    TextView attributionRequirements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sources);
        attributionRequirements = (TextView) findViewById(R.id.googleMapsAttributionRequirements);
        attributionRequirements.setText(GoogleApiAvailability.getInstance().getOpenSourceSoftwareLicenseInfo(this));

        sourcesMenu = (Button) findViewById(R.id.sourcesMenu);
        sourcesMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(Sources.this, MainActivity.class);
                startActivity(nextActivity);
            }
        });

    }

}
