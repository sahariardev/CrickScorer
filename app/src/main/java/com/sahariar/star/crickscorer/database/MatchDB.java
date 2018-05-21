package com.sahariar.star.crickscorer.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sahariar.star.crickscorer.Model.Match;
import com.sahariar.star.crickscorer.Model.PlayerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by STAR on 4/18/2018.
 */

public class MatchDB {

    private final String tableName="matches";


    //declear result
    //delte math
    //find mathces tounamntid

    public boolean add(SQLiteDatabase db, long team1_id,long team2_id,long tournament_id)
    {

        try {
            ContentValues match = new ContentValues();

            match.put("team1_id", team1_id);
            match.put("team2_id", team2_id);
            match.put("tournament_id", tournament_id);
            db.insert(tableName, null, match);


        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }

    public boolean delete(SQLiteDatabase db,int id)
    {
        MatchDB mdb=new MatchDB();

        try {
            //delete overs and and balls first


            mdb.deleteByTeamId(db,id);
            db.delete(tableName, "id=" + id, null);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    public boolean deleteByTeamId(SQLiteDatabase db,int id)
    {
        try {
            db.delete(tableName, "team1_id=" + id, null);
            return true;
        }
        catch(Exception e)
        {
            try{
                db.delete(tableName, "team2_id=" + id, null);
            }
            catch(Exception e2)
            {
                return false;
            }
            return false;
        }
    }

    public Match getOne(SQLiteDatabase db,long id)
    {
        Match m=new Match();
        String sql="SELECT * from "+tableName+" WHERE id ="+id;
        Cursor res=db.rawQuery(sql,null);
        res.moveToFirst();

        while(!res.isAfterLast())
        {
            m.setId((int)res.getLong(0));
            m.setTournament_id((int)res.getLong(1));
            m.setTeam1_id((int)res.getLong(2));
            m.setTeam2_id((int)res.getLong(3));
            m.setWinner_id(res.getInt(4));
            m.setResult(res.getString(5));
           res.moveToNext();
        }
        return m;
    }

    public String getMatchName(SQLiteDatabase db, Match match)
    {
        TeamDB team=new TeamDB();


        char name1[]=(team.getTeamName(db,match.getTeam1_id())).toCharArray();
        char name2[]=(team.getTeamName(db,match.getTeam2_id())).toCharArray();

        String name="";


        for(int i=0;i<name1.length;i++)
        {
            name=name+name1[i];
            if(i==2) break;
        }
        name=name+" v/s ";
        for(int i=0;i<name2.length;i++)
        {
            name=name+name2[i];
            if(i==2) break;
        }
        return name;

    }
    public List<Match> getAllMatchesNyTournamentId(SQLiteDatabase db, long tournament_id )
    {
        String sql="Select * from "+tableName+" where tournament_id = "+tournament_id ;
        Cursor res=db.rawQuery(sql,null);
        res.moveToFirst();

        List<Match> matches=new ArrayList();

        while(!res.isAfterLast())
        {


            Match match=new Match(Integer.parseInt(res.getString(2)),Integer.parseInt(res.getString(3)));
            match.setId((int)res.getLong(0));
            match.setTournament_id((int)res.getLong(1));
            matches.add(match);
            res.moveToNext();
        }


        return matches;
    }
    public boolean declear (SQLiteDatabase db,long id,long team_id, String description)
    {
        Log.v("Match",description+"  -");
        ContentValues match = new ContentValues();
         match.put("winner_id",team_id);
         match.put("result",description);
        db.update(tableName,match,"id = ?",new String[]{id+""});



        return true;
    }




}
