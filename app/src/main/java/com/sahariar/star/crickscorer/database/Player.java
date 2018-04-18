package com.sahariar.star.crickscorer.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sahariar.star.crickscorer.Model.PlayerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by STAR on 4/15/2018.
 */

public class Player {

    private final String tableName="players";
    private final String jointable="team_player";

    //addPlayer->boolean

    public boolean addPlayer(SQLiteDatabase db,String name)
    {

        try {
            ContentValues player = new ContentValues();
            player.put("name", name);
            db.insert(tableName, null, player);
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }


    //RemovePlayer->boolean
    public boolean deletePlayer(SQLiteDatabase db,int id)
    {

        try {
            db.delete(tableName, "id=" + id, null);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    //All player->all player

    public List<PlayerModel> getAllplayers(SQLiteDatabase db)
    {
        String sql="Select * from "+tableName;
        Cursor res=db.rawQuery(sql,null);
        res.moveToFirst();

        List<PlayerModel> players=new ArrayList();

       while(!res.isAfterLast())
        {
            PlayerModel pm=new PlayerModel(Integer.parseInt(res.getString(0)),res.getString(1));
            players.add(pm);
            res.moveToNext();
        }


        return players;
    }
    public List<PlayerModel> getOneTeamplayers(SQLiteDatabase db, long team_id)
    {

        String sql="Select * from "+tableName+" INNER JOIN "+jointable+" On "+tableName+".id = "+jointable+".player_id where team_id="+team_id;
        Log.v("Query Inner Join",sql);
        Cursor res=db.rawQuery(sql,null);
        res.moveToFirst();

        List<PlayerModel> players=new ArrayList();

        while(!res.isAfterLast())
        {
            PlayerModel pm=new PlayerModel(Integer.parseInt(res.getString(0)),res.getString(1));
            players.add(pm);
            res.moveToNext();
        }


        return players;
    }

    public  List<PlayerModel> getSpecificPlayer(SQLiteDatabase db,List<PlayerModel> players)
    {
        List<PlayerModel> allplayers=getAllplayers(db);
        List<PlayerModel> specificPlayer=new ArrayList<>();

         boolean flag=true;
        for(PlayerModel p:allplayers)
        {
            for(PlayerModel p1:players )
            {
                if(p1.getName().equals(p.getName()))
                {
                    Log.v("Team","Here");
                    flag=false;
                    break;
                }
            }
            if(flag)specificPlayer.add(p);
            flag=true;

        }





        return specificPlayer;
    }



}
