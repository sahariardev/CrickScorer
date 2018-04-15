package com.sahariar.star.crickscorer.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by STAR on 1/19/2018.
 */


public class Tournament {

    //TN stands for TABILE NAME
    public final  String TN="tournaments";

    public boolean Insert(SQLiteDatabase db,String name)
    {
        ContentValues tournament=new ContentValues();
        tournament.put("name",name);
        db.insert(TN,null,tournament);
        return true;
    }
    public String[] getTournamentList(SQLiteDatabase db)
    {


        Cursor res=db.rawQuery("Select * from tournaments",null);
        res.moveToFirst();
        String tournaments="";
        while(!res.isAfterLast())
        {
            tournaments=tournaments+res.getString(1)+"1111";
            res.moveToNext();
        }

        return tournaments.split("1111");
    }
}
