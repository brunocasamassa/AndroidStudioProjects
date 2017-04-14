package studio.brunocasamassa.retrofit;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bruno on 10/04/2017.
 */

public class ServiceGenerator {
    //URL base do endpoint. Deve sempre terminar com /
    public static final String API_BASE_URL = "https://randomuser.me/";

    public static <S> S createService(Class<S> serviceClass) {

        //Instancia do interceptador das requisições


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS);

        httpClient.addInterceptor(loggingInterceptor);
        //httpClient.addInterceptor(loggingInterceptor).build();


        //Instância do retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(httpClient.build())
                .build();



        return retrofit.create(serviceClass);
    }
}

