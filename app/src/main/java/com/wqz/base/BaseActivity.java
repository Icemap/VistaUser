package com.wqz.base;

import android.app.Activity;

/**
 * Created by Wqz on 2016/12/23.
 */

abstract public class BaseActivity extends Activity
{
    protected BaseApplication getBaseApplication()
    {
        return ((BaseApplication) getApplication());
    }
}
