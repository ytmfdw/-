package qf.com.qf.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.vinexs.mdicon.MaterialIcon;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import qf.com.qf.QFApplication;
import qf.com.qf.R;
import qf.com.qf.adapter.KaoQinAdapter;
import qf.com.qf.bean.KaoQinAllBean;
import qf.com.qf.bean.KaoQinBean;
import qf.com.qf.bean.UserBean;
import qf.com.qf.http.API;
import qf.com.qf.http.HtmlUtils;
import qf.com.qf.http.QueryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class QueryActivity extends AppCompatActivity {

    PtrFrameLayout refreshView;

    RecyclerView rv;

    List<KaoQinBean> data = new ArrayList<>();
    KaoQinAdapter adapter;

    int page = 1;

    private BottomSheetDialog mBottomSheetDialog;
    KaoQinAllBean allBean;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        //
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KaoQinAdapter(this, data);
        rv.setAdapter(adapter);

        refreshView = (PtrFrameLayout) findViewById(R.id.store_house_ptr_frame);
        refreshView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, rv, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page++;
                getData();
            }
        });

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MaterialIcon md = new MaterialIcon(this);
        md.setTheme(MaterialIcon.THEME_FREE);
//设置自由主题，非常重要，不然颜色设置无效
        md.setColor(Color.parseColor("#FFFFFF"));
        md.setAlpah(255);
        MenuItem item_all = menu.add(0, 1, 0, "汇总");
        item_all.setIcon(md.getMenuDrawable("ic_menu"));
        item_all.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: {
                //弹出汇总对话框
                if (allBean != null) {
                    if (mBottomSheetDialog == null) {
                        mBottomSheetDialog = new BottomSheetDialog(this);
                    }

                    TextView tv = new TextView(this);
                    tv.setPadding(10, 10, 0, 0);
                    tv.setText(allBean.toString());
                    mBottomSheetDialog.setContentView(tv);
                    mBottomSheetDialog.setTitle("出勤汇总");

                    //如果已经展示了，就退出，不然就显示
                    if (mBottomSheetDialog.isShowing()) {
                        mBottomSheetDialog.dismiss();
                    }
                    mBottomSheetDialog.show();
                }

            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.URL_BASE)
                .client(API.getOkHttpClient(this))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        QueryService qs = retrofit.create(QueryService.class);
        Call<String> queryCall = qs.query(page);

        queryCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String value = response.body().trim();
                Log.d("ytmfdw", "查询结果：" + value);
                //考勤汇总
                allBean = HtmlUtils.getKaoQinAllBeanByHtml(value);
                //考勤数据
                data.addAll(0, HtmlUtils.getKaoQinBeanByHtml(value));
                adapter.notifyDataSetChanged();

                refreshView.refreshComplete();
                //设置标题
                UserBean user = QFApplication.user;
                if (user != null) {
                    getSupportActionBar().setTitle(user.name + "\t" + user.id);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                refreshView.refreshComplete();
            }
        });
    }
}
