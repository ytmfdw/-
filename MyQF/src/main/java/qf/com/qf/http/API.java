package qf.com.qf.http;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class API {
    /**
     * 主机地址
     */
    public static final String URL_BASE = "http://oa.1000phone.net/";
    /**
     * 登录
     */
    public static final String URL_LOGIN = "oa.php/Admin/login/";
    /**
     * 查看考勤
     */
    public static final String URL_CLOCK = "oa.php/Group/PerAttRecords/p/{page}";


    public static OkHttpClient getOkHttpClient(Context context) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0")
                                .build();
                        return chain.proceed(request);
                    }
                }).cookieJar(new CookieManger(context)).build();
        return httpClient;
    }
}
