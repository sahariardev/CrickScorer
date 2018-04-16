package com.sahariar.star.crickscorer.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sahariar.star.crickscorer.Model.PlayerModel;
import com.sahariar.star.crickscorer.Model.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by STAR on 4/16/2018.
 */

public class TeamDB {

    //add team
    private final String tableName="teams";


    public long add(SQLiteDatabase db, String name)
    {

        try {
            ContentValues team = new ContentValues();
            team.put("name", name);
            return db.insert(tableName, null, team);
        }
        catch(Exception e)
        {
            return -1;
        }

    }
    public boolean delete(SQLiteDatabase db,int id)
    {

        try {
            db.delete(tableName, "id=" + id, null);
            PlayerToTeam ptm=new PlayerToTeam();
            ptm.delte(db,id);
            return ptm.delte(db,id);
        }
        catch(Exception e)
        {
            return false;
        }
    }


    //remove team
    //show all team
    public List<Team> getAllplayers(SQLiteDatabase db)
    {
        String sql="Select * from "+tableName;
        Cursor res=db.rawQuery(sql,null);
        res.moveToFirst();

        List<Team> teams=new ArrayList();

        while(!res.isAfterLast())
        {

            Team team=new Team(Integer.parseInt(res.getString(0)),res.getString(1));
            teams.add(team);
            res.moveToNext();
        }


        return teams;
    }
}
