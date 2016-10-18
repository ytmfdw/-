package qf.com.qf.utils;

import android.content.Context;
import android.content.SharedPreferences;

import qf.com.qf.bean.UserBean;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class SharedUtils {
    private static SharedPreferences shared;
    private static SharedUtils utils;

    private SharedUtils() {
    }

    public static SharedUtils getInstances(Context mContext) {
        if (shared == null) {
            shared = mContext.getSharedPreferences("shared_utils", Context.MODE_PRIVATE);
        }
        if (utils == null) {
            utils = new SharedUtils();
        }
        return utils;
    }

    public void saveUser(UserBean user) {
        shared.edit().putString("user", user.toString()).commit();
    }

    public UserBean getUser() {
        String value = shared.getString("user", null);
        if (value == null) {
            return null;
        }
        try {
            String[] arr = value.split(":");
            UserBean user = new UserBean();
            user.account = arr[0];
            user.password = arr[1];
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
