package qf.com.joke;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gc.materialdesign.widgets.Dialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import uk.co.senab.photoview.PhotoView;

public class MainActivity extends AppCompatActivity {

    /**
     * 每日一笑接口
     * <p/>
     * 可带参数：
     * <p/>
     * http://www.ytmfdw.com/coupon/index.php?c=user&a=getonejoke&data_id=20720
     */
    static final String http = "http://www.ytmfdw.com/coupon/index.php?c=user&a=getonejoke";

    Toolbar toolbar;
    TextView tv;
    ImageView img;
    PtrFrameLayout refreshView;

    JokeBean joke;
    /**
     * 状态栏高度
     */
    int statuesHeight = -1;

    boolean isKitkat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 沉浸 状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 沉浸 虚拟按键
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            isKitkat = true;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        if (isKitkat) {
            statuesHeight = DisplayTools.getStatusHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
            params.height = params.height + statuesHeight;
            toolbar.setPadding(0, statuesHeight, 0, 0);
            toolbar.setLayoutParams(params);
            //设置底部
//            findViewById(R.id.root).setPadding(0, 0, 0, DisplayTools.getBottomStatusHeight(this));

        }

        setSupportActionBar(toolbar);

        setupViews();

        //下载一个
        getJoke(null);

    }

    private void setupViews() {
        tv = (TextView) findViewById(R.id.tv_context);
        img = (ImageView) findViewById(R.id.img);
        refreshView = (PtrFrameLayout) findViewById(R.id.refresh);

        //初始时，img为不可见
        img.setVisibility(ImageView.GONE);
        //初始化refreshView
        refreshView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, img, header);
            }


            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getJoke(joke);
            }
        });
        //弹出图片对话框
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开查看图片界面
                if (img.getDrawable() != null) {
                    Intent intent = new Intent(MainActivity.this, LookPicture.class);
                    MainApplication.drawable = img.getDrawable().getCurrent();
                    startActivity(intent);
                }

            }
        });
    }

    private void getJoke(final JokeBean jokeBean) {

        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(JokeServer.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        JokeServer jokeServer = retrofit.create(JokeServer.class);

        Call<String> call;
        if (jokeBean != null) {
            call = jokeServer.getOneJoke(joke.data_id);
        } else {
            call = jokeServer.getOneJoke();
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String value = response.body().toString();
                try {
                    if (jokeBean == null) {

                        JSONObject json = new JSONObject(value);
                        joke = new JokeBean(json.getJSONObject("data"));
                    } else {
                        JSONObject json = new JSONObject(value);
                        JSONArray arr = json.optJSONArray("data");
                        joke = new JokeBean(arr.getJSONObject(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv.setText(joke.content);
                if (joke.img != null) {
                    img.setVisibility(View.VISIBLE);
                    Glide.with(MainActivity.this).load(joke.img).into(img);
                } else {
                    img.setVisibility(View.GONE);
                }
                refreshView.refreshComplete();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                refreshView.refreshComplete();
            }
        });
    }

    //点击再来一个
    public void getOne(View view) {
        getJoke(null);
    }


    public static OkHttpClient getOkHttpClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0")
                                .build();
                        return chain.proceed(request);
                    }
                }).build();
        return httpClient;
    }

}
