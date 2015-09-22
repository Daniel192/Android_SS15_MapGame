package android.mi.ur.de.android_ss15_mapgame.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.mi.ur.de.android_ss15_mapgame.utility.QuestionItem;

import java.util.ArrayList;

/**
 * Created by Daniel on 24.08.2015.
 */
public class QuestionDb {
    private static final String DATABASE_NAME = "questions.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TABLE = "questions";

    private static final String KEY_ID = "_id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_REGION = "region";

    private static final int COLUMN_ID_INDEX = 0;
    private static final int COLUMN_QUESTION_INDEX = 1;
    private static final int COLUMN_LATITUDE_INDEX = 2;
    private static final int COLUMN_LONGITUDE_INDEX = 3;
    private static final int COLUMN_ANSWER_INDEX = 4;
    private static final int COLUMN_REGION_INDEX = 5;

    private static final int GERMANY = 0;
    private static final int EUROPE = 1;
    private static final int WORLD = 2;


    private QuestionDbOpenHelper dbHelper;

    private SQLiteDatabase db;

    public QuestionDb(Context context) {
        dbHelper = new QuestionDbOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    // ID starts counting at 1, calling this with 0 will return null
     public QuestionItem getQuestionItem(int questionID) {
        QuestionItem item;
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_QUESTION, KEY_LATITUDE,KEY_LONGITUDE, KEY_ANSWER}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(COLUMN_ID_INDEX) == questionID) {
                    String question = cursor.getString(COLUMN_QUESTION_INDEX);
                    String answer = cursor.getString(COLUMN_ANSWER_INDEX);
                    float latitude = cursor.getFloat(COLUMN_LATITUDE_INDEX);
                    float longitude = cursor.getFloat(COLUMN_LONGITUDE_INDEX);
                    item = new QuestionItem(question, answer, latitude, longitude);
                    return item;
                }

            } while (cursor.moveToNext());
        }
        return null;
    }

    //Returns all QuestionItems in the database
    public ArrayList<QuestionItem> getAllQuestionItems() {
        ArrayList<QuestionItem> items = new ArrayList<QuestionItem>();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_QUESTION, KEY_LATITUDE, KEY_LONGITUDE, KEY_ANSWER}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String question = cursor.getString(COLUMN_QUESTION_INDEX);
                String answer = cursor.getString(COLUMN_ANSWER_INDEX);
                float latitude = cursor.getFloat(COLUMN_LATITUDE_INDEX);
                float longitude = cursor.getFloat(COLUMN_LONGITUDE_INDEX);

                items.add(new QuestionItem(question, answer, latitude, longitude));

            } while (cursor.moveToNext());
        }
        return items;
    }

    //Returns all QuestionItems for the specified region (germany = 0, europe = 1, world = 2)
    public ArrayList<QuestionItem> getAllQuestionItems(int region) {
        ArrayList<QuestionItem> items = new ArrayList<QuestionItem>();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_QUESTION, KEY_LATITUDE, KEY_LONGITUDE, KEY_ANSWER, KEY_REGION}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(COLUMN_REGION_INDEX) <= region) {
                    String question = cursor.getString(COLUMN_QUESTION_INDEX);
                    String answer = cursor.getString(COLUMN_ANSWER_INDEX);
                    float latitude = cursor.getFloat(COLUMN_LATITUDE_INDEX);
                    float longitude = cursor.getFloat(COLUMN_LONGITUDE_INDEX);

                    items.add(new QuestionItem(question, answer, latitude, longitude));
                }

            } while (cursor.moveToNext());
        }
        return items;
    }


    private class QuestionDbOpenHelper extends SQLiteOpenHelper {
        private final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_QUESTION +  " varchar(255), " + KEY_LATITUDE + " float, " + KEY_LONGITUDE + " float, " + KEY_ANSWER + " varchar(255), " + KEY_REGION + " integer);";

        public QuestionDbOpenHelper(Context c, String dbname, SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbname, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);

            db.execSQL(addQuestion("Wo ist Berlin?", 52.5075419f, 13.4251364f, "Berlin", GERMANY));
            db.execSQL(addQuestion("Wo ist Hamburg?", 53.558572f, 9.9278215f, "Hamburg", GERMANY));
            db.execSQL(addQuestion("Wo ist München?", 48.1549107f, 11.5418357f, "München", GERMANY));
            db.execSQL(addQuestion("Wo ist Düsseldorf?", 51.2384547f, 6.8143502f, "Düsseldorf", GERMANY));
            db.execSQL(addQuestion("Wo ist Dortmund?", 51.5078845f, 7.4702625f, "Dortmund", GERMANY));
            db.execSQL(addQuestion("Wo ist Bremen?", 53.1202572f, 8.7362858f, "Bremen", GERMANY));
            db.execSQL(addQuestion("Wo ist Hannover?", 52.3796664f, 9.7614715f, "Hannover", GERMANY));
            db.execSQL(addQuestion("Wo ist Duisburg?", 52.5075419f, 6.7279745f, "Duisburg", GERMANY));
            db.execSQL(addQuestion("Wo ist Bochum?", 51.4709074f, 7.2255577f, "Bochum", GERMANY));
            db.execSQL(addQuestion("Wo ist Bielefeld?", 52.0148115f, 8.5206095f, "Bielefeld", GERMANY));
            db.execSQL(addQuestion("Wo ist Bonn?", 50.703577f, 7.1157122f, "Bonn", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Augsburg?", 48.3583992f, 10.8614402f, "Augsburg", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Braunschweig?", 52.2721924f, 10.527885f, "Braunschweig", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Chemnitz?", 50.8226152f, 12.8909761f, "Chemnitz", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Aachen?", 50.75968f, 6.0965247f, "Aachen", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Krefeld?", 51.3456345f, 6.5920815f, "Krefeld", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Kiel?", 54.3418129f, 10.125677f, "Kiel", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Saarbrücken?", 49.2471573f, 6.9825383f, "Saarbrücken", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Leverkusen?", 51.0541742f, 7.006992f, "Leverkusen", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Solingen?", 51.1672852f, 7.0626138f, "Solingen", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Heidelberg?", 49.4057284f, 8.6836142f, "Heidelberg", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Paderborn?", 51.7276064f, 8.7684325f, "Paderborn", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Ingolstadt?", 48.7533744f, 11.3796516f, "Ingolstadt", GERMANY));
            db.execSQL(addQuestion("Wo liegt Wolfsburg?", 52.4055354f, 10.7769022f, "Wolfsburg", GERMANY));
            db.execSQL(addQuestion("Wo liegt Pforzheim?", 48.8743578f, 8.7167653f, "Pforzheim", GERMANY));
            db.execSQL(addQuestion("Wo liegt Göttingen?", 51.5369374f, 9.9268528f, "Göttingen", GERMANY));
            db.execSQL(addQuestion("Wo liegt Recklinghausen?", 51.6020641f, 7.2149296f, "Recklinghausen", GERMANY));
            db.execSQL(addQuestion("Wo liegt Koblenz?", 50.3462931f, 7.5885547f, "Koblenz", GERMANY));
            db.execSQL(addQuestion("Wo liegt Bremerhaven?", 53.543008f, 8.5827881f, "Bremerhaven", GERMANY));
            db.execSQL(addQuestion("Wo liegt Jena?", 50.922513f, 11.5859738f, "Jena", GERMANY));
            db.execSQL(addQuestion("Wo liegt Trier?", 49.7778813f, 6.6494598f, "Trier", GERMANY));
            db.execSQL(addQuestion("Wo liegt Passau?", 48.5769408f, 13.4083659f, "Passau", GERMANY));
            db.execSQL(addQuestion("Wo liegt Dresden?", 51.0768337f, 13.7725857f, "Dresden", GERMANY));




            db.execSQL(addQuestion("Wo liegt Altötting?", 48.2263996f, 12.6701338f, "Altötting", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Coburg?", 50.2603389f, 10.9755166f, "Coburg", GERMANY));
            db.execSQL(addQuestion("Wo ist Erfurt?", 50.9853404f, 11.0153355f, "Erfurt", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Frankfurt am Main?", 50.121212f, 8.6365638f, "Frankfurt am Main", GERMANY));
            db.execSQL(addQuestion("Wo ist Regensburg?", 48.9940947f, 12.0750918f, "Regensburg", GERMANY));
            db.execSQL(addQuestion("Wo liegt Heilbronn?", 49.1513079f, 9.1732881f, "Heilbronn", GERMANY));
            db.execSQL(addQuestion("Wo ist Karlsruhe?", 49.0158491f, 8.4095339f, "Karlsruhe", GERMANY));
            db.execSQL(addQuestion("Wo ist Köln?", 50.9572449f, 6.9673223f, "Köln", GERMANY));
            db.execSQL(addQuestion("Wo liegt Lübeck?", 53.8810006f, 10.7613749f, "Lübeck", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Münster?", 51.9500854f, 7.6240971f, "Münster", GERMANY));
            db.execSQL(addQuestion("Wo liegt Nürnberg?", 49.4360936f, 11.1011232f, "Nürnberg", GERMANY));
            db.execSQL(addQuestion("Wo ist Osnarbrück?", 52.2779866f, 8.0554295f, "Osnarbrück", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Potsdam?", 52.4283615f, 13.0274123f, "Potsdam", GERMANY));
            db.execSQL(addQuestion("Wo ist Stuttgart?", 48.7792090f, 9.1772152f, "Stuttgart", GERMANY));
            db.execSQL(addQuestion("Wo liegt Ulm?", 48.3875890f, 9.9424448f, "Ulm", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Tübingen?", 48.5221441f, 9.0477834f, "Tübingen", GERMANY));
            db.execSQL(addQuestion("Wo ist Würzburg?", 49.7780731f, 9.9430286f, "Würzburg", GERMANY));
            db.execSQL(addQuestion("Wo ist Wuppertal?", 51.2418916f, 7.1637667f, "Wuppertal", GERMANY));
           /* db.execSQL(addQuestion("Wo ist der Checkpoint Charlie?", 52.5075419f, 13.4251364f, "Berlin: Checkpoint Charlie", GERMANY));
            db.execSQL(addQuestion("Wo ist Goethes Wohnhaus?", 50.9769891f, 11.3184553f, "Weimar: Goethes Wohnhaus", GERMANY));
            db.execSQL(addQuestion("Wo ist der Hugenottenbrunnen?", 49.5891771f, 10.9844836f, "Erlangen: Hugenottenbrunnen", GERMANY));
            db.execSQL(addQuestion("Wo ist die Reeperbahn?", 53.5585720f, 9.9278215f, "Hamburg: Reeperbahn", GERMANY));
            db.execSQL(addQuestion("Wo ist das Schloss Neuschwanstein?", 47.5575740f, 10.7498004f, "Schloss Neuschwanstein", GERMANY));
            db.execSQL(addQuestion("Wo ist der Europa-Park?", 48.2660194f, 7.7220076f, "Europa-Park", GERMANY));
            db.execSQL(addQuestion("Wo findet das Oktoberfest statt?", 48.1549107f, 11.5418357f, "München: Oktoberfest", GERMANY));
            db.execSQL(addQuestion("Wo ist der Olympiapark?", 48.1549107f, 11.5418357f, "München: Olympiapark", GERMANY));
            db.execSQL(addQuestion("Wo ist die Semperoper?", 51.0768337f, 13.7725857f, "Dresden: Semperoper", GERMANY));
            db.execSQL(addQuestion("Wo ist der Englische Garten?", 48.1549107f, 11.5418357f, "München: Englischer Garten", GERMANY));
            db.execSQL(addQuestion("Wo befindet sich die Zugspitze?", 47.4207504f, 10.9854391f, "Zugspitze", GERMANY));
            db.execSQL(addQuestion("Wo ist das Dürerhaus?", 49.4360936f, 11.1011232f, "Nürnberg: Dürerhaus", GERMANY)); */

            db.execSQL(addQuestion("Wo liegt Madrid?", 40.4379543f, -3.6795367f, "Madrid", EUROPE));
            db.execSQL(addQuestion("Wo ist Rom?", 41.9100711f, 12.5359979f, "Rom", EUROPE));
            db.execSQL(addQuestion("Wo ist Wien?", 48.2206849f, 16.3800599f, "Wien", EUROPE));
            db.execSQL(addQuestion("Wo liegt Budapest?", 47.4812134f, 19.1303031f, "Budapest", EUROPE));
            db.execSQL(addQuestion("Wo befindet sich Warschau?", 52.232938f, 21.0611941f, "Warschau", EUROPE));
            db.execSQL(addQuestion("Wo ist Barcelona?", 41.39479f, 2.1487679f, "Barcelona", EUROPE));
            db.execSQL(addQuestion("Wo liegt Prag?", 50.0596696f, 14.4656239f, "Prag", EUROPE));
            db.execSQL(addQuestion("Wo ist Mailand?", 45.4627338f, 9.1777323f, "Mailand", EUROPE));
            db.execSQL(addQuestion("Wo ist Neapel?", 40.8539855f, 14.2466023f, "Neapel", EUROPE));
            db.execSQL(addQuestion("Wo liegt Amsterdam?", 52.3747158f, 4.8986142f, "Amsterdam", EUROPE));
            db.execSQL(addQuestion("Wo ist Athen?", 37.9908372f, 23.7383394f, "Athen", EUROPE));
            db.execSQL(addQuestion("Wo befindet sich Oslo?", 59.8938549f, 10.7851165f, "Oslo", EUROPE));
            db.execSQL(addQuestion("Wo ist Rotterdam?", 51.9279723f, 4.4904063f, "Rotterdam", EUROPE));
            db.execSQL(addQuestion("Wo ist Dublin?", 53.3243201f, -6.245704f, "Dublin", EUROPE));
            db.execSQL(addQuestion("Wo liegt Manchester?", 53.4722454f, -2.2235922f, "Manchester", EUROPE));
            db.execSQL(addQuestion("Wo ist Turin?", 45.070139f, 7.6700892f, "Turin", EUROPE));
            db.execSQL(addQuestion("Wo ist London?", 51.5286416f, -0.1015987f, "London", EUROPE));
            db.execSQL(addQuestion("Wo ist Kiew?", 50.4020355f, 30.5326905f, "Kiew", EUROPE));
            db.execSQL(addQuestion("Wo befindet sich Bukarest?", 44.4378258f, 26.0946376f, "Bukarest", EUROPE));
            db.execSQL(addQuestion("Wo ist Belgrad?", 44.8152453f, 20.4203223f, "Belgrad", EUROPE));

            db.execSQL(addQuestion("Wo ist Shanghai?", 31.2243489f, 121.4767528f, "Shanghai", WORLD));
            db.execSQL(addQuestion("Wo liegt Istanbul?", 41.0053215f, 29.0121795f, "Istanbul", WORLD));
            db.execSQL(addQuestion("Wo ist Mumbai?", 19.0822507f, 72.8812042f, "Mumbai", WORLD));
            db.execSQL(addQuestion("Wo ist Tokio?", 35.673343f, 139.710388f, "Tokio", WORLD));
            db.execSQL(addQuestion("Wo ist New York?", 40.7033127f, -73.979681f, "New York", WORLD));
            db.execSQL(addQuestion("Wo ist Kairo?", 30.0594885f, 31.2584644f, "Kairo", WORLD));
            db.execSQL(addQuestion("Wo ist Bangkok?", 13.7246005f, 100.6331108f, "Bangkok", WORLD));
            db.execSQL(addQuestion("Wo liegt Hongkong?", 22.3576782f, 114.1210181f, "Hongkong", WORLD));
            db.execSQL(addQuestion("Wo befindet sich Rio de Janeiro?", -22.9112335f, -43.448334f, "Rio de Janeiro", WORLD));
            db.execSQL(addQuestion("Wo ist Bagdad?", 33.311686f, 44.355905f, "Bagdad", WORLD));
            db.execSQL(addQuestion("Wo ist Ankara?", 39.9033766f, 32.7627648f, "Ankara", WORLD));
            db.execSQL(addQuestion("Wo liegt Los Angeles?", 34.0204989f, -118.4117325f, "Los Angeles", WORLD));
            db.execSQL(addQuestion("Wo ist Sidney?", 33.7969235f, 150.9224326f, "Sidney", WORLD));
            db.execSQL(addQuestion("Wo ist Singapur", 1.3147308f, 103.8470128f, "Singapur", WORLD));
            db.execSQL(addQuestion("Wo liegt Chicago?", 41.8337329f, -87.7321555f, "Chicago", WORLD));
            db.execSQL(addQuestion("Wo ist Buenos Aires", -34.6158238f, -58.4333203f, "Buenos Aires", WORLD));
            db.execSQL(addQuestion("Wo liegt Kapstadt?", -33.9149861f, 18.6560594f, "Kapstadt", WORLD));
            db.execSQL(addQuestion("Wo ist Houston?", 29.817178f, -95.4012915f, "Houston", WORLD));
            db.execSQL(addQuestion("Wo ist Phoenix?", 33.6054149f, -112.125051f, "Phoenix", WORLD));
            db.execSQL(addQuestion("Wo ist Moskau?", 55.749792f, 37.632495f, "Moskau", WORLD));
            db.execSQL(addQuestion("Wo liegt San Diego?", 32.8245525f, -117.0951632f, "San Diego", WORLD));
            db.execSQL(addQuestion("Wo befindet sich Dubai?", 24.979447f, 55.3127729f, "Dubai", WORLD));

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        private String addQuestion(String question, float latitude, float longitude, String answer, int region) {
            String insertClause = "insert into " + DATABASE_TABLE + " (" + KEY_QUESTION + ", " + KEY_LATITUDE + ", " + KEY_LONGITUDE + ", " + KEY_ANSWER + ", " + KEY_REGION + ") values ('" + question + "', " + latitude + ", " + longitude + ", '" + answer + "', " + region + ");";
            return insertClause;
        }

    }
}
