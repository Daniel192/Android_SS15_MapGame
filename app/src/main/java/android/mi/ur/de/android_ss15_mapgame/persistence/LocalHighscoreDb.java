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
    private static final String KEY_SCORE = "score";
    private static final String KEY_REGION = "region";

    private static final int GERMANY = 0;
    private static final int EUROPE = 1;
    private static final int WORLD = 2;

    private static final int COLUMN_SCORE_INDEX = 1;
    private static final int COLUMN_REGION_INDEX = 2;


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


    public long updateScore(int score, int region){
        String whereClause = KEY_REGION + " = " + region + ";";
        ContentValues newScoreValues = new ContentValues();
        newScoreValues.put(KEY_SCORE, score);
        return db.update(DATABASE_TABLE, newScoreValues, whereClause, null);
    }

    public int getScore(int region){
        int score = 0;
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_SCORE, KEY_REGION}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(COLUMN_REGION_INDEX) == region) {
                    score = cursor.getInt(COLUMN_SCORE_INDEX);
                }
            }while (cursor.moveToNext());
        }
        return score;
    }


    private class LocalHighscoreDbOpenHelper extends SQLiteOpenHelper {
        private final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_SCORE + " integer, " + KEY_REGION + " integer);";
        private final String DATABASE_INIT_REGION_1 = "insert into " + DATABASE_TABLE + " (" + KEY_SCORE + ", " + KEY_REGION + ") values (0, " + GERMANY + ");";
        private final String DATABASE_INIT_REGION_2 = "insert into " + DATABASE_TABLE + " (" + KEY_SCORE + ", " + KEY_REGION + ") values (0, " + EUROPE + ");";
        private final String DATABASE_INIT_REGION_3 = "insert into " + DATABASE_TABLE + " (" + KEY_SCORE + ", " + KEY_REGION + ") values (0, " + WORLD + ");";

        public LocalHighscoreDbOpenHelper(Context c, String dbname, SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbname, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_INIT_REGION_1);
            db.execSQL(DATABASE_INIT_REGION_2);
            db.execSQL(DATABASE_INIT_REGION_3);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
