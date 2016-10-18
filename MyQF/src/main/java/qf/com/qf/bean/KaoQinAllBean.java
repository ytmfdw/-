package qf.com.qf.bean;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * //部门 	岗位 	姓名 	应出勤天数 	实际出勤天数
 * 事假扣款(h) 	病假/h 	迟到20分钟 	迟到30分钟 	迟到60分钟 	迟到120分钟
 * 未打卡/次 	倒休/h 	带薪假/h 	累计加班/h 	带薪加班
 */
public class KaoQinAllBean {
    /**
     * 部门
     */
    public String department;
    /**
     * 岗位
     */
    public String post;
    /**
     * 姓名
     */
    public String name;
    /**
     * 应出勤天数
     */
    public int allDays;
    /**
     * 实际出勤天数
     */
    public int realDays;
    /**
     * 事假扣款(h)
     */
    public String deductions;
    /**
     * 病假/h
     */
    public int sickHours;
    /**
     * 迟到20分钟
     */
    public int late20;
    /**
     * 迟到30分钟
     */
    public int late30;
    /**
     * 迟到60分钟
     */
    public int late60;
    /**
     * 迟到120分钟
     */
    public int late120;
    /**
     * 未打卡/次
     */
    public int unsignCount;
    /**
     * 倒休/h
     */
    public int restHours;
    /**
     * 带薪假/h
     */
    public int restPayHours;
    /**
     * 累计加班/h
     */
    public int overtime;
    /**
     * 带薪加班
     */
    public int overtimePay;

    public KaoQinAllBean(Elements es) {
        try {
            department = es.get(0).text();
            post = es.get(1).text();
            name = es.get(2).text();
            allDays = Integer.valueOf(es.get(3).text());
            realDays = Integer.valueOf(es.get(4).text());
            deductions = es.get(5).text();
            sickHours = Integer.valueOf(es.get(6).text());
            late20 = Integer.valueOf(es.get(7).text());
            late30 = Integer.valueOf(es.get(8).text());
            late60 = Integer.valueOf(es.get(9).text());
            late120 = Integer.valueOf(es.get(10).text());
            unsignCount = Integer.valueOf(es.get(11).text());
            restHours = Integer.valueOf(es.get(12).text());
            restPayHours = Integer.valueOf(es.get(13).text());
            overtime = Integer.valueOf(es.get(14).text());
            overtimePay = Integer.valueOf(es.get(15).text());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return
                "部门：" + department + "\n" +
                        "岗位：" + post + "\n" +
                        "姓名：" + name + "\n" +
                        "应出勤天数：" + allDays + "\n" +
                        "实际出勤天数：" + realDays + "\n" +
                        "事假扣款：" + deductions + "\n" +
                        "病假h：" + sickHours + "\n" +
                        "迟到20分钟：" + late20 + "\n" +
                        "迟到30分钟：" + late30 + "\n" +
                        "迟到60分钟：" + late60 + "\n" +
                        "迟到120分钟：" + late120 + "\n" +
                        "未打卡/次：" + unsignCount + "\n" +
                        "倒休/h：" + restHours + "\n" +
                        "带薪假/h：" + restPayHours + "\n" +
                        "累计加班/h：" + overtime + "\n" +
                        "带薪加班：" + overtimePay;
    }
}
