package com.sahariar.star.crickscorer.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by STAR on 1/19/2018.
 */

public class DB extends SQLiteOpenHelper {


     public static  final String dbName="crickscorer.db";

     public DB(Context context)
     {
         super(context, dbName, null, 1);
         SQLiteDatabase db=this.getWritableDatabase();



     }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("Here I am","on the db");


        //creating all tables

        String tournaments="Create Table tournaments"+
                            "("+
                              "id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                              "extra INTEGER  ,"+
                               "name TEXT"
                              +");";

        String matches="Create Table matches"+
                "("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "tournament_id INTEGER,"+
                "team1_id INTEGER,"+
                "team2_id INTEGER,"+
                "winner_id INTEGER,"+
                "result TEXT"
                +");";

        String players="Create Table players   "+
                "("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "name TEXT,"+
                "description TEXT"
                +");";
        String balls="Create Table balls  "+
                "("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "baller_id INTEGER,"+
                "batsman_id INTEGER, "+
                "runs INTEGER,"+
                "type INTEGER,"+
                "match_id INTEGER, "+
                "over_id INTEGER "
                +");";

        String overs="Create Table overs "+
                "("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "ballingTeam_id INTEGER ,"+
                "battingTeam_id INTEGER"
                +");";
        String teams="Create Table teams "+
                  "("+
                  "id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                  "name Text"
                  +");";

        String team_player="Create Table team_player "+
                "("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "team_id INTEGER,"+
                "player_id INTEGER"
                +");";




        db.execSQL(tournaments);
        db.execSQL(matches);
        db.execSQL(players);
        db.execSQL(balls);
        db.execSQL(overs);
        db.execSQL(teams);
        db.execSQL(team_player);







    }

    public String test()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from tournaments",null);
        res.moveToFirst();

        while(!res.isAfterLast())
        {
            return res.getString(1);
            //res.moveToNext();
        }
        return "404";
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("Drop Table IF EXIST "+"tournaments");
        onCreate(db);
    }
}
