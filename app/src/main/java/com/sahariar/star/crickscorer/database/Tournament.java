package com.sahariar.star.crickscorer.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sahariar.star.crickscorer.Model.TournamentModel;

import java.util.ArrayList;
import java.util.List;

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
        tournament.put("extra",1);
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
            tournaments=tournaments+res.getString(2)+"1111";
            res.moveToNext();
        }

        return tournaments.split("1111");
    }

    public TournamentModel getOne(SQLiteDatabase db,long id)
    {
        Cursor res=db.rawQuery("Select * from tournaments where id = "+id,null);
        res.moveToFirst();

        TournamentModel tm=new TournamentModel();
        while(!res.isAfterLast())
        {
            tm.setId(res.getLong(0));
            tm.setExtra(res.getInt(1));
            tm.setName(res.getString(1));
            res.moveToNext();
        }
        return tm;
    }
    public List<TournamentModel> getAll(SQLiteDatabase db)
    {
        List<TournamentModel>  tournaments=new ArrayList<>();
        Cursor res=db.rawQuery("Select * from tournaments",null);
        res.moveToFirst();
        while(!res.isAfterLast())
        {
            TournamentModel tm=new TournamentModel(res.getLong(0),res.getString(2));
            tournaments.add(tm);
            res.moveToNext();
        }
        return tournaments;
    }
    public boolean updateRules (SQLiteDatabase db,long id,int extra)
    {

        ContentValues match = new ContentValues();
        match.put("extra",extra);

        db.update("tournaments",match,"id = ?",new String[]{id+""});



        return true;
    }

}
