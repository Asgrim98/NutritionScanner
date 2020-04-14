package com.example.nutritionscanner;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nutritionscanner.DBHelper;

public class Provider extends ContentProvider {

    private DBHelper dbHelper;

    private static final String PROVIDER_ID = "com.example.NutritionScannerDB.Provider";

    public static final Uri URI_ZAWARTOSCI = Uri.parse("content://" + PROVIDER_ID + "/" + DBHelper.TABLE);

    private static final int TABLE = 1;
    private static final int ROW = 2;

    private static final UriMatcher uriMatch = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatch.addURI(PROVIDER_ID, DBHelper.TABLE, TABLE);
        uriMatch.addURI(PROVIDER_ID, DBHelper.TABLE + "/#", ROW);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int typUri = uriMatch.match(uri);

        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase DB = dbHelper.getWritableDatabase();

        Cursor kursor = null;

        switch (typUri) {
            case TABLE:
                kursor = DB.query(true, dbHelper.TABLE, projection, selection, selectionArgs, null, null, sortOrder, null);
                break;
            case ROW:

                break;
            default:
                throw new IllegalArgumentException("Unkown URI: " + uri);
        }

        kursor.setNotificationUri( getContext().getContentResolver(), uri );

        return kursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int uriType = uriMatch.match(uri);

        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase DB = dbHelper.getWritableDatabase();

        if( uriType == TABLE ){

            DB.insert( DBHelper.TABLE, null, values);
        } else {

            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int uriType = uriMatch.match(uri);
        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase DB = dbHelper.getWritableDatabase();

        if( uriType == TABLE ){
            DB.delete(DBHelper.TABLE, selection, selectionArgs);

        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri,null);

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int uriType = uriMatch.match(uri);

        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase DB = dbHelper.getWritableDatabase();

        if( uriType == TABLE ){
            DB.update(DBHelper.TABLE, values, selection, selectionArgs);

        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri,null);

        return 0;
    }
}
