package Device_info;

public interface OnGetMotorListListener {
    void onGetMotorListSuccess(String [] smotor_list);
    void onGetMotorListError();
}
