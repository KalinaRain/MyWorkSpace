package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KalinaRain on 2015/5/8.
 */
public class MyDBOpenHelper extends SQLiteOpenHelper {

    /**
     *  Province表的建表语句
     */
    public static final String CREATE_PROVINCE = "create Table Province()";



    public MyDBOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
