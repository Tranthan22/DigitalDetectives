package MainActivity;

public class Users {
    private int id;

    private String user_name,password,email,phone_number;

    public Users(int id, String user_name,String password, String email, String phone_number){
        this.id =id;
        this.user_name = user_name;
        this.password=password;
        this.email = email;
        this.phone_number = phone_number;
    }
    private boolean success;
    private String message;

    // Constructors, getters, setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
