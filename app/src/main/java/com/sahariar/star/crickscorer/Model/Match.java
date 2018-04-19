package com.sahariar.star.crickscorer.Model;

/**
 * Created by STAR on 4/18/2018.
 */

public class Match {

    private int id;
    private int team1_id;
    private int team2_id;
    private int winner_id;
    private int tournament_id;
    private String result;

    public Match(int team1_id, int team2_id) {
        this.team1_id = team1_id;
        this.team2_id = team2_id;
    }
    public String getName()
    {
        return "";
    }

    public int getId() {
        return id;
    }

    public int getTeam1_id() {
        return team1_id;
    }

    public int getTeam2_id() {
        return team2_id;
    }

    public int getWinner_id() {
        return winner_id;
    }

    public String getResult() {
        return result;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTeam1_id(int team1_id) {
        this.team1_id = team1_id;
    }

    public void setTeam2_id(int team2_id) {
        this.team2_id = team2_id;
    }

    public void setWinner_id(int winner_id) {
        this.winner_id = winner_id;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
