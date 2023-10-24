package Device_info;

import java.util.List;

public class Sensor_list {


    private List<String> sensor;
    public List<String> getSensor(){
        return sensor;
    }

    public void setSensor (List<String> sensor){
        this.sensor = sensor;
    }
    private String user_nameg;
    public Sensor_list(String user_nameg){
        String user_nameg1 = this.user_nameg;
    }
    public String getUser_nameg(){
        return user_nameg;
    }
    private boolean success;
    public boolean isSuccess(){
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
