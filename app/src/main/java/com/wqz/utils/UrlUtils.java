package com.wqz.utils;

/**
 * Created by Administrator on 2016/12/23.
 */

public class UrlUtils
{
    public static String BASE_URL = "http://192.168.1.115:8080/vista";

    //manager
    public static String BASE_MANAGER = BASE_URL + "/manager";
    public static String MANAGER_LOGIN = BASE_MANAGER + "/login";

    //proj
    public static String BASE_PROJ = BASE_URL + "/proj";
    public static String PROJ_SELECT_BY_MANAGERID = BASE_PROJ + "/selectByManagerId";

    //user
    public static String BASE_USER = BASE_URL + "/user";
    public static String USER_CREATE = BASE_USER + "/create";

    //vista
    public static String BASE_VISTA = BASE_URL + "/vista";
    public static String VISTA_SELECT_BY_PROJID = BASE_VISTA + "/selectByProjId";

    //html
    public static String HTML_VISTA = BASE_URL + "/html/getVista";
}
