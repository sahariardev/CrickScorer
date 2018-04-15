package com.sahariar.star.crickscorer.Model;

/**
 * Created by STAR on 4/15/2018.
 */

public class PlayerModel {

    private int id;
    private String name;
    private String description;

    public PlayerModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
