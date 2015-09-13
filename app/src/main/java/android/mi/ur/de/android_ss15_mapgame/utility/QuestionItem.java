package android.mi.ur.de.android_ss15_mapgame.utility;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Daniel on 31.08.2015.
 */
public class QuestionItem {

    private String question;
    private LatLng targetLocation;
    private String answer;

    public QuestionItem(String question, String answer, float latitude, float longitude){
        this.question = question;
        this.answer = answer;
        targetLocation = new LatLng(latitude, longitude);
    }

    public String getQuestion(){
        return question;
    }

    public LatLng getTargetLocation(){
        return targetLocation;
    }

    public String getAnswer() {
        return answer;
    }

}
