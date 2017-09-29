package studio.brunocasamassa.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by bruno on 04/04/2017.
 */

public interface Network {

    @GET("api/")
    Call<User> randomUser();

}
