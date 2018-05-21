package com.sahariar.star.crickscorer.Model;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by STAR on 5/9/2018.
 */

public class PlayerDetail {

    private long id;
    private String name;
    private int sixes;
    private int fours;
    private int wickets;
    private String overs;
    private int totalballplayed;
    private int totalrunsscored;
    private int runsgiven;
    private double economy;

    public int getTotalballplayed() {
        return totalballplayed;
    }

    public void setTotalballplayed(int totalballplayed) {
        this.totalballplayed = totalballplayed;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSixes(int sixes) {
        this.sixes = sixes;
    }

    public void setFours(int fours) {
        this.fours = fours;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public void setOvers(String overs) {
        this.overs = overs;
    }

    public void setTotalrunsscored(int totalrunsscored) {
        this.totalrunsscored = totalrunsscored;
    }

    public void setRunsgiven(int runsgiven) {
        this.runsgiven = runsgiven;
    }

    public void setEconomy(double economy) {
        this.economy = economy;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSixes() {
        return sixes;
    }

    public int getFours() {
        return fours;
    }

    public int getWickets() {
        return wickets;
    }

    public String getOvers() {
        return overs;
    }

    public int getTotalrunsscored() {
        return totalrunsscored;
    }

    public int getRunsgiven() {
        return runsgiven;
    }

    public String getEconomy() {
        NumberFormat formatter = new DecimalFormat("#0.0");
        return formatter.format(economy);
    }
}
