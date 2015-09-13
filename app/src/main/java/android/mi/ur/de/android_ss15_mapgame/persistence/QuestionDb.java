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
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_QUESTION, KEY_LATITUDE, KEY_LONGITUDE, KEY_ANSWER}, null, null, null, null, null);
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
            db.execSQL(addQuestion("Wo ist der Checkpoint Charlie?", 52.5075419f, 13.4251364f, "Berlin: Checkpoint Charlie", GERMANY));
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
            db.execSQL(addQuestion("Wo ist das Dürerhaus?", 49.4360936f, 11.1011232f, "Nürnberg: Dürerhaus", GERMANY));
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
