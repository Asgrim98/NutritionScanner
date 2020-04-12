package com.example.nutritionscanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DBHelper extends SQLiteOpenHelper {


    public final static int VERSION = 1;
    public final static String ID = "_id";
    public final static String DB_NAME = "NutritionScannerDB";
    public final static String TABLE = "Products2";
    public final static String COL1 = "productName";
    public final static String COL2 = "kcal";
    public final static String COL3 = "fat";
    public final static String COL4 = "carbohydrates";
    public final static String COL5 = "proteins";
    public final static String COL6 = "link";
    //public final static String SELECT_ROW = "SELECT * FROM " + TABLE + " WHERE columnName = ?";


    public final static String CREATE = "CREATE TABLE " + TABLE + " ( "
            + ID + " integer PRIMARY KEY autoincrement, "
            + COL1 + " TEXT NOT NULL, "
            + COL2 + " TEXT NOT NULL, "
            + COL3 + " TEXT NOT NULL, "
            + COL4 + " TEXT NOT NULL, "
            + COL5 + " TEXT NOT NULL, "
            + COL6 + " TEXT NOT NULL );";

    public final static String DROP_TAB = "DROP TABLE IF EXISTS " + TABLE + ";";

    public DBHelper(Context context){

        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

       // sqLiteDatabase.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

       // sqLiteDatabase.execSQL(DROP_TAB);
       // onCreate(sqLiteDatabase);
    }
}
