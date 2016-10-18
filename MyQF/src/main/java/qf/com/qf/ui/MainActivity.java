package qf.com.qf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import qf.com.qf.R;
import qf.com.qf.SharedUtils;
import qf.com.qf.bean.UserBean;
import qf.com.qf.http.API;
import qf.com.qf.http.HttpUtils;
import qf.com.qf.http.LoginService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText et_name, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);

        //获取保存的值
        UserBean user = SharedUtils.getInstances(this).getUser();
        if (user != null) {
            try {
                et_name.setText(user.account);
                et_password.setText(user.password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //点击登录
    public void doLogin(View view) {

//        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
//        okHttpClient.interceptors().add(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//
//                // Request customization: add request headers
//                Request.Builder requestBuilder = original.newBuilder()
//                        .header("Content-Type", "application/x-www-form-urlencoded")
//                        .header("Connection", "keep-alive")
//                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//                        .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
//
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        });
        OkHttpClient okHttpClient = API.getOkHttpClient(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();
        LoginService ls = retrofit.create(LoginService.class);
        //==================for test============
       /* String name = "xiechun@1000Phone.com";
        String password = "0607020088Xie";
        Call<String> loginCall = ls.login(name, password);*/
        //=============test end=========================
        Call<String> loginCall = ls.login(et_name.getText().toString(), et_password.getText().toString());
        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String value = response.body().trim();
                String reg = "[^\u4e00-\u9fa5]";
                value = value.replaceAll(reg, "");
//                Log.d("ytmfdw", "登录结果：");
                Log.d("ytmfdw", "登录结果：value=" + value);
                if (!value.contains("失败") && !value.contains("删除")) {
                    //保存用户输入的值
                    UserBean user = new UserBean();
                    user.account = et_name.getText().toString();
                    user.password = et_password.getText().toString();
                    SharedUtils.getInstances(MainActivity.this).saveUser(user);
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, QueryActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


      /*  new Thread() {
            @Override
            public void run() {
                try {
                    String value = HttpUtils.postDataByUrl(API.URL_BASE + API.URL_LOGIN, "AdminName=" + et_name.getText().toString() + "&PassWord=" + et_password.getText().toString());
                    Log.d("ytmfdw", "登录结果：" + value);
                    if (!value.contains("失败") && !value.contains("删除")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, QueryActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();*/

    }
}
