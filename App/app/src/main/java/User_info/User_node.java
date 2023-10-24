package User_info;

import java.util.List;

public class User_node {
    private List<String> tien;

    public List<String> getTien() {
        return tien;
    }

    public void setTien(List<String> tien) {
        this.tien = tien;
    }
    private String user_namen;

   public User_node(String user_namen){
       String user_namen1 = this.user_namen;
   }
   public String getUser_namen(){
       return user_namen;
   }
    private String nodeInfo;

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

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
