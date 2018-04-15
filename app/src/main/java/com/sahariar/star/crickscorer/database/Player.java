package com.sahariar.star.crickscorer.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sahariar.star.crickscorer.Model.PlayerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by STAR on 4/15/2018.
 */

public class Player {

    private final String tableName="players";

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


}
