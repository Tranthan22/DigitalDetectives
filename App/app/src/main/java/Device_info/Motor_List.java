package Device_info;

import java.util.List;

public class Motor_List {

    private List<String > motor;
    public List<String> getMotor(){
        return motor;
    }
    public void setMotor (List<String> motor) {
        this.motor = motor;
    }
    private String user_nameg;
    public Motor_List(String user_nameg){
        String user_name1 = this.user_nameg;
    }
    private boolean success;
    public boolean isSuccess(){
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
