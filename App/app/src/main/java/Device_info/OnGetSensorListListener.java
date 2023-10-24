package Device_info;

public interface OnGetSensorListListener {
    void onGetSensorListSuccess(String[] ssensor_list);
    void onGetSensorListError();
}
