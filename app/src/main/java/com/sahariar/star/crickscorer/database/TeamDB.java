package com.sahariar.star.crickscorer.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
       MatchDB m=new MatchDB();

        try {
             if(m.deleteByTeamId(db,id)) {


                 db.delete(tableName, "id=" + id, null);
                 PlayerToTeam ptm = new PlayerToTeam();
                 ptm.delte(db, id);
                 return ptm.delte(db, id);
             }
             else
             {
                 return false;
             }
        }
        catch(Exception e)
        {
            return false;
        }
    }
    public String getTeamName(SQLiteDatabase db,long id)
    {
        String sql="Select * from "+tableName+" WHERE id = "+id;

        Cursor res=db.rawQuery(sql,null);
        res.moveToFirst();

        Team team=new Team(0,null);

        while(!res.isAfterLast())
        {


            team=new Team(Integer.parseInt(res.getString(0)),res.getString(1));

            res.moveToNext();
        }

        return team.getName();
    }
    public Team getOne(SQLiteDatabase db,long id)
    {
        String sql="Select * from "+tableName+" WHERE id = "+id;

        Cursor res=db.rawQuery(sql,null);
        res.moveToFirst();

        Team team=new Team(0,null);

        while(!res.isAfterLast())
        {


            team=new Team(Integer.parseInt(res.getString(0)),res.getString(1));

            res.moveToNext();
        }

        return team;
    }


    //remove team
    //show all team
    public List<Team> getAllTeams(SQLiteDatabase db)
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
