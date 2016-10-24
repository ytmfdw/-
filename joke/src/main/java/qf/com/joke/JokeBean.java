package qf.com.joke;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/22 0022.
 */
public class JokeBean {

    public JokeBean() {
    }

    public JokeBean(JSONObject json) {
        data_id = json.optString("data_id");
        content = json.optString("content");
        img = json.optString("img");
        good = json.optInt("good");
    }

    public String data_id;
    public String content;
    public String img;
    public int good;
}
