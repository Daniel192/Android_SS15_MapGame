package android.mi.ur.de.android_ss15_mapgame.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;

/**
 * Created by Daniel on 24.08.2015.
 */
public class LocalHighscoreDb {


    private static final String DATABASE_NAME = "localHighscore.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TABLE = "localHighscore";

    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";

    private static final int COLUMN_NAME_INDEX = 1;
    private static final int COLUMN_SCORE_INDEX = 2;


    private LocalHighscoreDbOpenHelper dbHelper;

    private SQLiteDatabase db;

    public LocalHighscoreDb(Context context) {
        dbHelper = new LocalHighscoreDbOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    public long updateName(String name){
        ContentValues newScoreValues = new ContentValues();
        newScoreValues.put(KEY_NAME, name);
        return db.update(DATABASE_TABLE, newScoreValues, null, null);
    }

    public long updateScore(int score){
        ContentValues newScoreValues = new ContentValues();
        newScoreValues.put(KEY_SCORE, score);
        return db.update(DATABASE_TABLE, newScoreValues, null, null);
    }

    public String getName(){
        String name = "";
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_SCORE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
           name = cursor.getString(COLUMN_NAME_INDEX);
        }
        return name;
    }

    public int getScore(){
        int score = 0;
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_SCORE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            score = cursor.getInt(COLUMN_SCORE_INDEX);
        }
        return score;
    }


    private class LocalHighscoreDbOpenHelper extends SQLiteOpenHelper {
        private final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_NAME +  " varchar(255), " + KEY_SCORE + " integer);";
        private final String DATABASE_INIT = "insert into " + DATABASE_TABLE + " (" + KEY_NAME + ", " + KEY_SCORE + ") values ('Spieler', 0);";

        public LocalHighscoreDbOpenHelper(Context c, String dbname, SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbname, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_INIT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
