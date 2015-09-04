package android.mi.ur.de.android_ss15_mapgame.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.mi.ur.de.android_ss15_mapgame.utility.QuestionItem;

/**
 * Created by Daniel on 24.08.2015.
 */
public class QuestionDb {
    private static final String DATABASE_NAME = "questions.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TABLE = "questions";

    private static final String KEY_ID = "_id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_LOCATION_1 = "location1";
    private static final String KEY_LOCATION_2 = "location2";

    private static final int COLUMN_ID_INDEX = 0;
    private static final int COLUMN_QUESTION_INDEX = 1;
    private static final int COLUMN_LOCATION_1_INDEX = 2;
    private static final int COLUMN_LOCATION_2_INDEX = 3;


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

    public long addQuestion(String question, float location1, float location2) {
        ContentValues newQuestionValues = new ContentValues();
        newQuestionValues.put(KEY_QUESTION, question);
        newQuestionValues.put(KEY_LOCATION_1, location1);
        newQuestionValues.put(KEY_LOCATION_1, location2);
        return db.insert(DATABASE_TABLE, null, newQuestionValues);
    }

    /*this part will be included as soon as QuestionItems are implemented

     public QuestionItem getQuestionItem(int questionID) {
        QuestionItem item;
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_QUESTION, KEY_LOCATION_1,KEY_LOCATION_2}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(COLUMN_ID_INDEX) == questionID) {
                    String question = cursor.getString(COLUMN_QUESTION_INDEX);
                    float location1 = cursor.getFloat(COLUMN_LOCATION_1_INDEX;
                    float location2 = cursor.getFloat(COLUMN_LOCATION_2_INDEX;
                    item = new QuestionItem(question, location1, location2);
                    return item;
                }

            } while (cursor.moveToNext());
        }
        return null;
    }*/


    private class QuestionDbOpenHelper extends SQLiteOpenHelper {
        private final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_QUESTION + " varchar(255), " + KEY_LOCATION_1 + " float, " + KEY_LOCATION_2 + " float);";

        public QuestionDbOpenHelper(Context c, String dbname, SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbname, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
