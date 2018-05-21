package com.sahariar.star.crickscorer.Model;

import java.util.List;

/**
 * Created by STAR on 4/16/2018.
 */

public class Team {

    private int id;
    private String name;
    private List<PlayerModel> players;

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<PlayerModel> getPlayers() {
        return players;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(List<PlayerModel> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
