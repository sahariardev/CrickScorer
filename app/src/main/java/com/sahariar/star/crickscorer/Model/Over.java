package com.sahariar.star.crickscorer.Model;

/**
 * Created by STAR on 5/3/2018.
 */

public class Over {
    private long id;
    private long ballingTeamId;
    private long battingTeamId;

    public Over(long ballingTeamId, long battingTeamId) {
        this.ballingTeamId = ballingTeamId;
        this.battingTeamId = battingTeamId;
    }

    public long getBallingTeamId() {
        return ballingTeamId;
    }

    public long getBattingTeamId() {
        return battingTeamId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBallingTeamId(long ballingTeamId) {
        this.ballingTeamId = ballingTeamId;
    }

    public void setBattingTeamId(long battingTeamId) {
        this.battingTeamId = battingTeamId;
    }
}
