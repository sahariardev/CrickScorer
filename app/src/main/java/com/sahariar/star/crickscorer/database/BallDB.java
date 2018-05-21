package com.sahariar.star.crickscorer.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sahariar.star.crickscorer.Model.Ball;
import com.sahariar.star.crickscorer.Model.PlayerDetail;
import com.sahariar.star.crickscorer.Model.PlayerModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by STAR on 5/3/2018.
 */

public class BallDB {

    private String tableName="balls";
    private String joinTable="overs";

    public boolean add(SQLiteDatabase db, Ball ball)
    {

        try {
            ContentValues b = new ContentValues();
            b.put("baller_id", ball.getBaller_id());
            b.put("batsman_id", ball.getBatsman_id());
            b.put("match_id", ball.getMatch_id());
            b.put("over_id", ball.getOver_id());
            b.put("runs", ball.getRuns());
            b.put("type", ball.getType());
            Log.v("Runs","triggered");
            db.insert(tableName, null, b);
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }

    public List<Ball> getAllBallsByMatchTeam(SQLiteDatabase db, long match_id,long team_id)
    {
        String sql="SELECT * from "+tableName+" INNER JOIN "+joinTable+" On "+tableName+".over_id ="+joinTable+".id  where match_id ="+match_id+" AND battingTeam_id="+team_id;
        Cursor res=db.rawQuery(sql,null);
        res.moveToFirst();
        List<Ball>   balls=new ArrayList<>();
        int overCount=-1;
        int currentover=0;
        int ballcount=1;
        while(!res.isAfterLast())
        {
            long overId=res.getLong(7);
            if(overId !=currentover) {
                currentover = (int)overId;
                overCount++;
                ballcount=1;
            }


            Ball ball=new Ball(res.getLong(0),res.getLong(1),res.getLong(2),(int)res.getLong(3),(int)res.getLong(4),res.getLong(5),res.getLong(6));

            ball.setBallNUmber(overCount+"."+ballcount);

            balls.add(ball);
            ballcount++;
            if(ball.getType()==1||ball.getType()==2)
            {
                ballcount--;
            }
           res.moveToNext();
        }
        return  balls;


    }
    public PlayerDetail getPlayerDetailForOneMatch(SQLiteDatabase db,PlayerDetail player,long match_id)
    {
        String sql="SELECT type,runs from "+tableName +" Where batsman_id = "+player.getId()+" AND match_id= "+match_id;
        Cursor res=db.rawQuery(sql,null);
        res.moveToFirst();
        int totalruns=0;
        int toalballs=0;
        int numFours=0;
        int numSix=0;



        while(!res.isAfterLast())
        {

            int type=res.getInt(0);
            int runs=res.getInt(1);


            if(type==2)
            {
                totalruns=totalruns+runs-1;
                if(runs-1==4)
                {
                    numFours++;
                }
                else if(runs-1==6)
                {
                    numSix++;
                }
            }
            else if(type==1)
            {
                //nothing to do
            }
            else
            {
                if(runs==4)
                {
                    numFours++;
                }
                else if(runs==6)
                {
                    numSix++;
                }
                totalruns=totalruns+runs;
            }

            if(type!=1)
            {
                toalballs++;
            }
            res.moveToNext();
        }

        player.setFours(numFours);
        player.setSixes(numSix);
        player.setTotalrunsscored(totalruns);
        player.setTotalballplayed(toalballs);

        //second query
        sql="SELECT type,runs,over_id from "+tableName +" Where baller_id = "+player.getId()+" AND match_id= "+match_id;
        res=db.rawQuery(sql,null);
        res.moveToFirst();
        int totalrungiven=0;
        int ballcount=0;
        int currentover=0;
        int overCount=-1;
        int allballs=0;
        int wickets=0;
        while(!res.isAfterLast())
        {
            allballs++;
            long overId=res.getLong(2);
            if(overId !=currentover) {
                currentover = (int)overId;
                overCount++;
                ballcount=1;
            }
            else
            {
                ballcount++;
            }
            Log.v("Summery","Ball count"+ballcount);

            totalrungiven=totalrungiven+res.getInt(1);

            if(res.getInt(0)==5)
            {
              wickets++;
            }
            if(res.getInt(0)==1 || res.getInt(0)==2 )
            {
                allballs--;
                ballcount--;
            }
            res.moveToNext();
        }
        if(allballs==0)
        {
            allballs=1;
        }
        if(overCount==-1)
        {
            overCount=0;
        }
        double eco=totalrungiven/(double)allballs*6;
        player.setEconomy(eco);
        player.setRunsgiven(totalrungiven);
        player.setOvers(overCount+"."+ballcount);
        player.setWickets(wickets);
        return player;


    }

    public List<PlayerDetail> getSameTeamPlayersDetail(SQLiteDatabase db,long team_id,long match_id)
    {
        Player player=new Player();
        List<PlayerModel> players=player.getOneTeamplayers(db,team_id);

        List<PlayerDetail> playerDetails=new ArrayList<>();
        for(PlayerModel p: players)
        {
            PlayerDetail pd=new PlayerDetail();
            pd.setId(p.getId());
            pd.setName(p.getName());
            pd=getPlayerDetailForOneMatch(db,pd,match_id);
            playerDetails.add(pd);
        }
        return playerDetails;

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
    public boolean deleteByMatchId(SQLiteDatabase db, long id)
    {
        try {


            db.delete(tableName, "match_id=" + id, null);
            return true;

        }
        catch(Exception e)
        {
            return false;
        }
    }



}
