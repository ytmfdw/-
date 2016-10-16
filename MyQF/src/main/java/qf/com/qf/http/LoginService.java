package qf.com.qf.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public interface LoginService {
    @FormUrlEncoded
    @POST(API.URL_LOGIN)
    Call<String> login(@Field("AdminName") String name, @Field("PassWord") String password);
}
