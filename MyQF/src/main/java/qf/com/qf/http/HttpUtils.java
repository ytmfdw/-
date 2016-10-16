package qf.com.qf.http;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import qf.com.qf.QFApplication;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class HttpUtils {

    public static String postDataByUrl(String http, String params) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(http).openConnection();
        conn.setRequestMethod("POST");// 提交模式
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");//添加头
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//添加头
        conn.setRequestProperty("Connection", "keep-alive");//添加头
        // conn.setConnectTimeout(10000);//连接超时 单位毫秒
        // conn.setReadTimeout(2000);//读取超时 单位毫秒
        conn.setDoOutput(true);// 是否输入参数
        byte[] bypes = params.toString().getBytes();
        conn.getOutputStream().write(bypes);// 输入参数
        InputStream inStream = conn.getInputStream();
        //保存Cookie
        QFApplication.cookieStr = conn.getHeaderField("Set-Cookie");
        Log.d("ytmfdw", "Cookie:" + QFApplication.cookieStr);
        return convertStreamToString(inStream);
    }


    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * Sync Cookie
     */
    public static void syncCookie(Context context, String url) {
        try {
            Log.d("ytmfdw", url);

            CookieSyncManager.createInstance(context);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                Log.d("ytmfdw", oldCookie);
            }

            StringBuilder sbCookie = new StringBuilder();
//            PHPSESSID=h568gfr1it2m81t966a2lqbsm1; path=/
            String[] arr = QFApplication.cookieStr.split(";");
            sbCookie.append(String.format("PHPSESSID=%s", arr[0].split("=")[1]));
//            sbCookie.append(String.format(";domain=%s", "INPUT YOUR DOMAIN STRING"));
            sbCookie.append(String.format(";path=%s", arr[1].split("=")[1]));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();

            String newCookie = cookieManager.getCookie(url);
            if (newCookie != null) {
                Log.d("ytmfdw", newCookie);
            }
        } catch (Exception e) {
            Log.e("ytmfdw", e.toString());
        }
    }
}
