package ind.sq.android.todo.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ActivityDB extends SQLiteOpenHelper {
	private static final String DB_NAME = "activity_db";
	private static final int DB_VERSION = 1;
	private static final String TABLE_NAME = "activities";
	
	public static final String FIELD_ID = "_id";
	public static final String FIELD_TEXT = "activity_text";
	
	public ActivityDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE_NAME + " (" + FIELD_ID + " INTEGER primary key autoincrement, "
			+ " " + FIELD_TEXT + " text)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists " + TABLE_NAME;
		db.execSQL(sql);
		
		onCreate(db);
	}
	public int delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(TABLE_NAME, FIELD_ID+ " = ?", new String[]{String.valueOf(id)});
	}
	
	public int insert(String text) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(FIELD_TEXT, text);
		long id = db.insert(TABLE_NAME, null, cv);
		return (int)id;
	}
	
	public int update(int id, String text) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(FIELD_TEXT, text);
		return db.update(TABLE_NAME, cv, FIELD_ID+ " = ?", new String[]{String.valueOf(id)});
	}
	
	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}
}
