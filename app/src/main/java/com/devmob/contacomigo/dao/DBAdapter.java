package com.devmob.contacomigo.dao;

/**
 * Created by silviomm on 15/05/17.
 */

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class DBAdapter {

    protected static final String TAG = "DBAdapter";
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    protected static final String CREATE_PESSOA =
            "CREATE TABLE Pessoa(" +
                    "id INTEGER PRIMARY KEY, " +
                    "nome VARCHAR(255) NOT NULL, " +
                    "precoTotal REAL NOT NULL " +
                    ");";
    protected static final String CREATE_PRODUTO =
            "CREATE TABLE Produto(" +
                    "id INTEGER PRIMARY KEY," +
                    "nome VARCHAR(255) NOT NULL, " +
                    "preco REAL NOT NULL" +
                    ");";
    protected static final String CREATE_PESSOAPRODUTO =
            "CREATE TABLE PessoaProduto(" +
                    "id INTEGER PRIMARY KEY, " +
                    "idPessoa INTEGER, " +
                    "idProduto INTEGER," +
                    "FOREIGN KEY(idPessoa) REFERENCES Pessoa(id)," +
                    "FOREIGN KEY(idProduto) REFERENCES Produto(id)" +
                    ");";

    protected static final String DATABASE_NAME = "ContaComigo";
    protected static final int DATABASE_VERSION = 100;

    protected final Context mCtx;

    protected static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_PESSOA);
            db.execSQL(CREATE_PRODUTO);
            db.execSQL(CREATE_PESSOAPRODUTO);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Pessoa");
            db.execSQL("DROP TABLE IF EXISTS Produto");
            db.execSQL("DROP TABLE IF EXISTS PessoaProduto");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public DBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open or create the routes database.
     *
     * @return this
     * @throws SQLException if the database could be neither opened or created
     */
    public DBAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(mCtx);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

}
