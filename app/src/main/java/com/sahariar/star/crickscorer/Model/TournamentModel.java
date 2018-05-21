package com.sahariar.star.crickscorer.Model;

/**
 * Created by STAR on 5/2/2018.
 */

public class TournamentModel {
    private long id;
    private String name;
    private int extra;

    public TournamentModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getExtra() {
        return extra;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }

    public TournamentModel() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
