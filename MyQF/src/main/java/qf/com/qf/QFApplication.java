package qf.com.qf;

import android.app.Application;

import qf.com.qf.bean.UserBean;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class QFApplication extends Application {

    public static String cookieStr = null;
    public static UserBean user = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
