package User_info;

public class Node_info {
    private String node_auto, node_air, node_soil, temp, pump_1, pump_2, pump_3, battery, time, date,
    bumpTime, daily_pumpTime,airWarning, soilWarning, battery_warning,sensor_id,motor_id,
            mode,tempWarning;
    public Node_info(String[] nodeInfo) {
        this.node_auto = nodeInfo[0];
        this.node_air  = nodeInfo[1];
        this.node_soil = nodeInfo[2];
        this.temp   = nodeInfo[3];
        this.pump_1 = nodeInfo[4];
        this.pump_2 = nodeInfo[5];
        this.pump_3    = nodeInfo[6];
        this.battery   = nodeInfo[7];
        this.time      = nodeInfo[8];
        this.date      = nodeInfo[9];
        this.airWarning = nodeInfo[10];
        this.soilWarning = nodeInfo[11];
        this.battery_warning = nodeInfo[12];
        this.bumpTime = nodeInfo[13];
        this.daily_pumpTime = nodeInfo[14];

        this.sensor_id = nodeInfo[15];
        this.motor_id = nodeInfo[16];
        this.mode = nodeInfo[17];
        this.tempWarning = nodeInfo[18];
    }

    public String getAirWarning() {
        return airWarning;
    }

    public String getBattery_warning() {
        return battery_warning;
    }
     public String getSoilWarning() {
        return soilWarning;
    }
    public String getDaily_pumpTime() {
        return daily_pumpTime;
    }
    public String getMotor_id(){
        return motor_id;
    }
    public String getSensor_id(){
        return sensor_id;
    }
    public String getMode(){
        return mode;
    }
    public String getTempWarning(){
        return tempWarning;
    }



    public String getNode_auto() {
        return node_auto;
    }

    public String getBattery() {
        return battery;
    }

    public String getPump_1() {
        return pump_1;
    }

    public String getPump_2() {
        return pump_2;
    }

    public String getPump_3() {
        return pump_3;
    }

    public String getDate() {
        return date;
    }

    public String getTemp() {
        return temp;
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
