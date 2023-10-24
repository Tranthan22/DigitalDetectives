package MainActivity;

public class Users_check {

    private String username_check, password_check;
    public Users_check(String username_check, String password_check){
        String username_check1 = this.username_check;
        String password_check1 = this.password_check;
    }

    private boolean success;
    private boolean fail;
    private String message;

    public boolean isSuccess() {
        return success;
    }
    public boolean isFail(){
        return fail;
    }
    public String getMessage(){
        this.message = message;
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public void setFail(boolean fail) {
        this.fail = fail;
    }

    public String getUser_name() {
        return username_check;
    }
    public void setUser_name(String user_name) {
        this.username_check = user_name;
    }

    public String getPassword() {
        return password_check;
    }
    public void setPassword(String password) {
        this.password_check = password;
    }
}
