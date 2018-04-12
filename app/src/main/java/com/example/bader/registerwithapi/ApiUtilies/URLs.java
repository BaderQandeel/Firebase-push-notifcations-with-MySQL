package com.example.bader.registerwithapi.ApiUtilies;

/**
 * Created by Bader on 2017-09-25.
 */

public class URLs {
    //PUT YOUR HOST INSTEAD OF 192...
    private static final String ROOT_URL = "http://192.168.1.113/pro1/Api.php?apicall=";
    private static final String ROOT_URL2 = "http://192.168.1.113/pro1/";
    public static final String URL_SENDSINGLE_NOTIFY = ROOT_URL2+"sendSinglePush.php";
    public static final String URL_SENDMULTI_NOTIFY = ROOT_URL2+"sendMultiplePush.php";
    public static final String URL_GET_REGISTERED_FCM = ROOT_URL2 + "GetRegisteredDevices.php";
    public static final String URL_REGISTER_FCM = ROOT_URL + "RegisterDevice";
    public static final String URL_UPD_REGISTER_FCM = ROOT_URL + "updDevice";
    public static final String URL_UN_REGISTER_FCM = ROOT_URL + "DeleteDevice";
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";


}
