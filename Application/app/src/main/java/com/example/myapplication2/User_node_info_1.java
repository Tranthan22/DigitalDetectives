package com.example.myapplication2;

import java.util.List;

public class User_node_info_1 {
    private String node_auto, node_air, node_soil, node_PH, bump_1, bump_2, bump_3, battery, time, date;
    private List<String> garden;

    public List<String> getGarden() {
        return garden;
    }

    public void setTien(List<String> tien) {
        this.garden = garden;
    }
    private String user_nameg, node_infog;

    public User_node_info_1(String user_nameg, String node_infog){
        String user_namen1 = this.user_nameg;
        String node_info1 = this.node_infog;
    }
    public String getUser_namen(){
        return user_nameg;
    }
    public String getNode_infog(){
        return node_infog;
    }

    private String nodeInfo;

    public User_node_info_1(String[] nodeInfo) {
        this.node_auto = nodeInfo[0];
        this.node_air = nodeInfo[1];
        this.node_soil = nodeInfo[2];
        this.node_PH = nodeInfo[3];
        this.bump_1 = nodeInfo[4];
        this.bump_2 = nodeInfo[5];
        this.bump_3 = nodeInfo[6];
        this.battery = nodeInfo[7];
        this.time = nodeInfo[8];
        this.date = nodeInfo[9];
    }

    public String getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(String nodeInfo) {
        this.nodeInfo = nodeInfo;
    }
    private boolean success;
    public boolean isSuccess() {
        return success;
    }
}
