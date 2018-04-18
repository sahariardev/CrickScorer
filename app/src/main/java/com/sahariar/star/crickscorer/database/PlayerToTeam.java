package com.sahariar.star.crickscorer.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by STAR on 4/16/2018.
 */

public class PlayerToTeam {

    //add player_id and team_id
    private final String tableName="team_player";


    public long add(SQLiteDatabase db, long player_id,long team_id)
    {
        Log.v("Team idontheonhedb",team_id+""+player_id);
        try {
            ContentValues team = new ContentValues();
            team.put("player_id", player_id);
            team.put("team_id",team_id);
            return db.insert(tableName, null, team);
        }
        catch(Exception e)
        {
            return -1;
        }

    }
    public boolean delte(SQLiteDatabase db, long team_id)
    {
        try {
            db.delete(tableName, "team_id=" + team_id, null);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }

    }
    public boolean deleteByPlayerId(SQLiteDatabase db, long player_id)
    {
        try {
            db.delete(tableName, "player_id=" + player_id, null);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

}
