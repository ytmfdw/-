package qf.com.qf.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import qf.com.qf.R;
import qf.com.qf.adapter.KaoQinAdapter;
import qf.com.qf.bean.KaoQinBean;
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
public class QueryActivity extends Activity {

    PtrFrameLayout refreshView;

    RecyclerView rv;

    List<KaoQinBean> data = new ArrayList<>();
    KaoQinAdapter adapter;

    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
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
                data.addAll(0, HtmlUtils.getKaoQinBeanByHtml(value));
                adapter.notifyDataSetChanged();

                refreshView.refreshComplete();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                refreshView.refreshComplete();
            }
        });
    }
}
