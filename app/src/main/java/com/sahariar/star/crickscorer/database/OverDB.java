package com.sahariar.star.crickscorer.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.sahariar.star.crickscorer.Model.Ball;
import com.sahariar.star.crickscorer.Model.Over;


/**
 * Created by STAR on 5/3/2018.
 */

public class OverDB {

    private String tableName="overs";

    public long add(SQLiteDatabase db,Over o)
    {
        long l=-1;
        try {
            ContentValues over = new ContentValues();

            over.put("ballingTeam_id",o.getBallingTeamId());
            over.put("battingTeam_id",o.getBattingTeamId());

            return db.insert(tableName, null, over);
        }
        catch(Exception e)
        {
            return l;
        }

    }

    public boolean delete(SQLiteDatabase db, long id)
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
}
