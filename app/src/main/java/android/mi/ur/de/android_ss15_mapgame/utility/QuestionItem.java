package android.mi.ur.de.android_ss15_mapgame.utility;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Daniel on 31.08.2015.
 */
public class QuestionItem {

    private String question;
    private LatLng targetLocation;

    public QuestionItem(String question, float latitude, float longitude){
        this.question = question;
        targetLocation = new LatLng(latitude, longitude);
    }

    public String getQuestion(){
        return question;
    }

    public LatLng getTargetLocation(){
        return targetLocation;
    }

}
