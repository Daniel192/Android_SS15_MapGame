package android.mi.ur.de.android_ss15_mapgame.game;

public class ScoreCalculator {

    private  int VERY_FAR_AWAY = 150000;
    private  int FAR_AWAY = 125000;
    private  int AWAY = 100000;
    private  int CLOSE = 75000;
    private  int VERY_CLOSE = 50000;
    private  int ON_TARGET = 25000;

    private double result;

    private void setValues(int region){
        switch (region){
            case 0:
                ON_TARGET = 25000;
                VERY_CLOSE = 50000;
                CLOSE = 75000;
                AWAY = 100000;
                FAR_AWAY = 125000;
                VERY_FAR_AWAY = 150000;
                break;
            case 1:
                ON_TARGET = 50000;
                VERY_CLOSE = 100000;
                CLOSE = 150000;
                AWAY = 200000;
                FAR_AWAY = 200000;
                VERY_FAR_AWAY = 300000;
                break;
            case 2:
                ON_TARGET = 75000;
                VERY_CLOSE = 150000;
                CLOSE = 225000;
                AWAY = 300000;
                FAR_AWAY = 375000;
                VERY_FAR_AWAY = 450000;
                break;
        }
    }

    public double calculateScore(double distance, int region){
        setValues(region);

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

        } else {
            result = 0;

        }
        return result;
    }
}
