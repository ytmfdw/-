package qf.com.joke;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/10/22 0022.
 */
public interface JokeServer {

    static final String BASE_URL = "http://www.ytmfdw.com/";
    static final String ONEJOKE_URL = "coupon/index.php?c=user&a=getonejoke";
//    static final String ONEJOKE_BYID_URL = "coupon/index.php?c=user&a=getonejoke&data_id={data_id}";

    @GET(ONEJOKE_URL)
    Call<String> getOneJoke();

    @GET(ONEJOKE_URL)
    Call<String> getOneJoke(@Query("data_id") String data_id);
}
