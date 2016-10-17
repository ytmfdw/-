package qf.com.qf.bean;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import qf.com.qf.QFApplication;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class KaoQinBean {
    /**
     * 日期 	工号 	姓名 	部门 	打卡方式 	打卡时间 	考勤设备
     */
    public String kq_date;
    public String kq_id;
    public String kq_name;
    public String kq_dpet;
    public String kq_type;
    public String kq_time;
    public String kq_address;

    public KaoQinBean() {
    }

    public KaoQinBean(Elements e) {
        try {
            kq_date = e.get(0).text();
            kq_id = e.get(1).text();
            kq_name = e.get(2).text();
            kq_dpet = e.get(3).text();
            kq_type = e.get(4).text();
            kq_time = e.get(5).text();
            kq_address = e.get(6).text();
            if (QFApplication.user == null) {
                QFApplication.user = new UserBean();
                QFApplication.user.name = kq_name;
                QFApplication.user.id = kq_id;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return kq_date + ":" + kq_address;
    }
}
