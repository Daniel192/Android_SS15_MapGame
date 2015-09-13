package android.mi.ur.de.android_ss15_mapgame.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
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
    private static final String KEY_REGION = "region";

    private static final int COLUMN_ID_INDEX = 0;
    private static final int COLUMN_QUESTION_INDEX = 1;
    private static final int COLUMN_LATITUDE_INDEX = 2;
    private static final int COLUMN_LONGITUDE_INDEX = 3;
    private static final int COLUMN_REGION_INDEX = 4;

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
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_QUESTION, KEY_LATITUDE,KEY_LONGITUDE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(COLUMN_ID_INDEX) == questionID) {
                    String question = cursor.getString(COLUMN_QUESTION_INDEX);
                    float latitude = cursor.getFloat(COLUMN_LATITUDE_INDEX);
                    float longitude = cursor.getFloat(COLUMN_LONGITUDE_INDEX);
                    item = new QuestionItem(question, latitude, longitude);
                    return item;
                }

            } while (cursor.moveToNext());
        }
        return null;
    }

    //Returns all QuestionItems in the database
    public ArrayList<QuestionItem> getAllQuestionItems() {
        ArrayList<QuestionItem> items = new ArrayList<QuestionItem>();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_QUESTION, KEY_LATITUDE, KEY_LONGITUDE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String question = cursor.getString(COLUMN_QUESTION_INDEX);
                float latitude = cursor.getFloat(COLUMN_LATITUDE_INDEX);
                float longitude = cursor.getFloat(COLUMN_LONGITUDE_INDEX);

                items.add(new QuestionItem(question, latitude, longitude));

            } while (cursor.moveToNext());
        }
        return items;
    }

    //Returns all QuestionItems for the specified region (germany = 0, europe = 1, world = 2)
    public ArrayList<QuestionItem> getAllQuestionItems(int region) {
        ArrayList<QuestionItem> items = new ArrayList<QuestionItem>();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_QUESTION, KEY_LATITUDE, KEY_LONGITUDE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(COLUMN_REGION_INDEX) <= region) {
                    String question = cursor.getString(COLUMN_QUESTION_INDEX);
                    float latitude = cursor.getFloat(COLUMN_LATITUDE_INDEX);
                    float longitude = cursor.getFloat(COLUMN_LONGITUDE_INDEX);

                    items.add(new QuestionItem(question, latitude, longitude));
                }

            } while (cursor.moveToNext());
        }
        return items;
    }


    private class QuestionDbOpenHelper extends SQLiteOpenHelper {
        private final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_QUESTION +  " varchar(255), " + KEY_LATITUDE + " float, " + KEY_LONGITUDE + " float, " + KEY_REGION + " integer);";

        public QuestionDbOpenHelper(Context c, String dbname, SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbname, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(addQuestion("Wo liegt Altötting?", 48.2263996f, 12.6701338f, GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Coburg?", 50.2603389f, 10.9755166f, GERMANY));
            db.execSQL(addQuestion("Wo ist Erfurt?", 50.9853404f, 11.0153355f, GERMANY));
            db.execSQL(addQuestion("Wo befindet sich Frankfurt am Main?", 50.121212f, 8.6365638f, GERMANY));
            db.execSQL(addQuestion("Wo ist Regensburg?", 48.9940947f, 12.0750918f, GERMANY));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        private String addQuestion(String question, float latitude, float longitude, int region) {
            String insertClause = "insert into " + DATABASE_TABLE + " (" + KEY_QUESTION + ", " + KEY_LATITUDE + ", " + KEY_LONGITUDE + ", " + KEY_REGION + ") values ('" + question + "', " + latitude + ", " + longitude + ", " + region + ");";
            return insertClause;
        }

    }
}
