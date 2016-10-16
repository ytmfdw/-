package qf.com.qf.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public interface QueryService {
    @GET(API.URL_CLOCK)
    Call<String> query(@Path("page") int page);
}
