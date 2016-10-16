package qf.com.qf.http;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import qf.com.qf.bean.KaoQinBean;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class HtmlUtils {

    public static List<KaoQinBean> getKaoQinBeanByHtml(String html) {
        ArrayList<KaoQinBean> beans = new ArrayList<>();
        Document value = Jsoup.parseBodyFragment(html);
        Log.d("ytmfdw", "parseBody valueSize=");
        Elements es = value.getElementsByClass("table-responsive");
        if (es.size() == 2) {
            //合格，第一个是考勤汇总，第二个是个人考勤详情
            Element e_all = es.get(0);
            Elements e_all_body = e_all.getElementsByTag("tr");
            Elements e_all_value = e_all_body.get(2).getElementsByTag("td");
            StringBuilder sb_all = new StringBuilder();
            for (Element e : e_all_value) {
                sb_all.append(e.text()).append("\t");
            }
            Log.d("ytmfdw", "e_all.toString=" + sb_all.toString());
            //解析打卡详情=============================================
            Element e_kq = es.get(1);
            Element e_kq_value = e_kq.getElementsByTag("tbody").get(0);
            Elements e_tmp = e_kq_value.getElementsByTag("tr");
            for (Element e : e_tmp) {
                StringBuilder sb_tmp = new StringBuilder();
                Elements tds = e.getElementsByTag("td");
             /*   for (Element td : tds) {
                    sb_tmp.append(td.text()).append("\t");
                }*/
                KaoQinBean bean = new KaoQinBean(tds);
                beans.add(bean);
            }
            Log.d("ytmfdw", "e_kq.toString=" + beans.toString());
        }
        return beans;
    }
}
