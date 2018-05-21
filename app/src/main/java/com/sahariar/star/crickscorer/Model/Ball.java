package com.sahariar.star.crickscorer.Model;

/**
 * Created by STAR on 5/3/2018.
 */

public class Ball {

    private long id;
    private long baller_id;
    private long batsman_id;
    private long match_id;
    private long over_id;
    private int runs;
    private int type;//0 mean by batsman 1 means wide,2 means no ball. 3 means bounce 5 means out
    private  String ballNUmber;
    public Ball()
    {

    }

    public Ball(long id, long baller_id, long batsman_id,int runs, int type, long match_id, long over_id) {
        this.id = id;
        this.baller_id = baller_id;
        this.batsman_id = batsman_id;
        this.match_id = match_id;
        this.over_id = over_id;
        this.runs = runs;
        this.type = type;
    }

    public void setBallNUmber(String ballNUmber) {
        this.ballNUmber = ballNUmber;
    }

    public String getBallNUmber() {
        return ballNUmber;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBaller_id(long baller_id) {
        this.baller_id = baller_id;
    }

    public void setBatsman_id(long batsman_id) {
        this.batsman_id = batsman_id;
    }

    public void setMatch_id(long match_id) {
        this.match_id = match_id;
    }

    public void setOver_id(long over_id) {
        this.over_id = over_id;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public long getBaller_id() {
        return baller_id;
    }

    public long getBatsman_id() {
        return batsman_id;
    }

    public long getMatch_id() {
        return match_id;
    }

    public long getOver_id() {
        return over_id;
    }

    public int getRuns() {
        return runs;
    }

    public int getType() {
        return type;
    }
}
