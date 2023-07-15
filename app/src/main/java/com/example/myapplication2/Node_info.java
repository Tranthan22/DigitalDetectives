package com.example.myapplication2;

public class Node_info {
    private String node_auto, node_air, node_soil, node_PH, bump_1, bump_2, bump_3, battery, time, date,bumpTime;
    public Node_info(String[] nodeInfo) {
        this.node_auto = nodeInfo[0];
        this.node_air  = nodeInfo[1];
        this.node_soil = nodeInfo[2];
        this.node_PH   = nodeInfo[3];
        this.bump_1    = nodeInfo[4];
        this.bump_2    = nodeInfo[5];
        this.bump_3    = nodeInfo[6];
        this.battery   = nodeInfo[7];
        this.time      = nodeInfo[8];
        this.date      = nodeInfo[9];
        this.bumpTime = nodeInfo[10];
    }

    public String getNode_auto() {
        return node_auto;
    }

    public String getBattery() {
        return battery;
    }

    public String getBump_1() {
        return bump_1;
    }

    public String getBump_2() {
        return bump_2;
    }

    public String getBump_3() {
        return bump_3;
    }

    public String getDate() {
        return date;
    }

    public String getNode_PH() {
        return node_PH;
    }

    public String getNode_air() {
        return node_air;
    }

    public String getNode_soil() {
        return node_soil;
    }

    public String getTime() {
        return time;
    }

    public String getBumpTime(){
        return bumpTime;
    }
}
