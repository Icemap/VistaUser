package com.wqz.base;

import android.app.Application;

import com.wqz.pojo.Manager;
import com.wqz.pojo.Proj;
import com.wqz.pojo.UserHold;

/**
 * Created by Wqz on 2016/12/23.
 */

public class BaseApplication extends Application
{
    Manager manager;
    Proj proj;
    String rlt;

    public String age;
    public String income;
    public String homeAddress;
    public String workAddress;
    public String vistaMatrix;
    public String hold;

    public Manager getManager()
    {
        return manager;
    }

    public void setManager(Manager manager)
    {
        this.manager = manager;
    }

    public Proj getProj()
    {
        return proj;
    }

    public void setProj(Proj proj)
    {
        this.proj = proj;
    }

    public String getRlt()
    {
        return rlt;
    }

    public void setRlt(String rlt)
    {
        this.rlt = rlt;
    }
}
