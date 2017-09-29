package studio.brunocasamassa.retrofit2;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by bruno on 10/04/2017.
 */

public interface RetrofitService {
    @Headers("apikey: ")

    @FormUrlEncoded
    @POST("convert")
    Call<RespostaServidor> converterUnidade(@Field("from-type") String from_type,
                                            @Field("from-value") String from_value,
                                            @Field("to-type") String to_type);
}
