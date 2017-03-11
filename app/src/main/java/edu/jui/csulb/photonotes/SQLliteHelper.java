package edu.jui.csulb.photonotes;

import java.util.ArrayList; import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jmd19 on 3/9/2017.
 */

public class SQLliteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private  static final String DATABASE_NAME="photoNotes";
    private static final String TABLE_LABELS = "photoList";
    private static final String KEY_ID = "id";
    private static final String KEY_CAPTION = "caption";
    private static final String IMAGE="image";

    public SQLliteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_CAPTION + " TEXT," + IMAGE+ " TEXT)";
        sqLiteDatabase.execSQL(CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);
        onCreate(sqLiteDatabase);
    }

    public boolean addtoList(Note data){
        SQLiteDatabase sql=this.getWritableDatabase();
        Cursor cursor=null;

        ContentValues values=new ContentValues();
        values.put(KEY_ID, data.getId());
        values.put(KEY_CAPTION,data.getCaption());
        values.put(IMAGE,data.getImage());

        long save_id=sql.insert(TABLE_LABELS,null,values);
        sql.close();

        if (save_id>0)
            return true;
        else
            return false;
    }

    public ArrayList<Note> getCaptionList(){
        ArrayList<Note> lists=new ArrayList<Note>();
        SQLiteDatabase sql=this.getWritableDatabase();
        Cursor cursor=null;
        try {
            String query= "select * from " +TABLE_LABELS;
            cursor = sql.rawQuery(query,null);
            while (cursor.moveToNext()){
                Note getSet= new Note();
                getSet.id=cursor.getString(0);
                getSet.caption=cursor.getString(1);
                getSet.image=cursor.getString(2);
                lists.add(getSet);
            }
        }catch(Exception e){
                System.out.print(e.toString());
        }
        return lists;
    }

    public Note getImage(String id){
        Note getSet= new Note();
        SQLiteDatabase sql=this.getWritableDatabase();
        Cursor cursor=null;
        try{
            String query=" SELECT * FROM " +TABLE_LABELS+ " WHERE " +KEY_ID+ " = '" + id + "'" ;
            cursor = sql.rawQuery(query,null);
            if (cursor.moveToFirst()){
                getSet.id=cursor.getString(0);
                getSet.caption=cursor.getString(1);
                getSet.image=cursor.getString(2);
            }
        }catch (Exception e){
            System.out.print(e.toString());
        }
        return getSet;
    }
}

