package android.mi.ur.de.android_ss15_mapgame.game;

/**
 * Created by Daniel on 24.08.2015.
 */
public class ScoreCalculator {

    private static final int NO_POINTS = 0;
    private static final int VERY_FAR_AWAY = 500000;
    private static final int FAR_AWAY = 350000;
    private static final int AWAY = 200000;
    private static final int CLOSE = 100000;
    private static final int VERY_CLOSE = 50000;
    private static final int ON_TARGET = 20000;

    private double result;

    public double calculateScore(double distance){

        if(distance <= ON_TARGET){
            result = 100;

        } else if (distance <= VERY_CLOSE) {
            result = 90;

        } else if (distance <= CLOSE) {
            result = 80;

        } else if (distance <= AWAY) {
            result = 60;

        } else if (distance <= FAR_AWAY) {
            result = 40;

        } else if (distance <= VERY_FAR_AWAY) {
            result = 20;

        } else if (distance <= NO_POINTS) {
            result = 0;

        }
        return result;
    }
}
