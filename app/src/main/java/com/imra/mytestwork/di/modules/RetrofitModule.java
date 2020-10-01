package com.imra.mytestwork.di.modules;

import android.content.Context;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    public static String BASE_URL = "https://reqres.in/api/";

    @Provides
    @Singleton
    public Retrofit provideRetrofit (Retrofit.Builder builder) {
        return builder.baseUrl(BASE_URL).build();
    }

    @Provides
    @Singleton
    public Retrofit.Builder provideRetrofitBuilder (OkHttpClient.Builder httpClient) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());
    }

    @Provides
    @Singleton
    public OkHttpClient.Builder provideHttpClient(Cache cache, ConnectionSpec spec) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);
        httpClient
                .connectionSpecs(Collections.singletonList(spec))
                //.addInterceptor(logging)
                .cache(cache);
        return httpClient;
    }

//    @Provides
//    @Singleton
//    public HttpLoggingInterceptor provideHttpLoggingInterceptor () {
//        return new HttpLoggingInterceptor()
//                .setLevel(HttpLoggingInterceptor.Level.BODY);
//    }

    @Provides
    @Singleton
    public Cache provideCache(Context context){
        int cacheSize = 1024*1024*10;
        return new Cache(context.getCacheDir(),cacheSize);
    }

    //for api level 19
    @Provides
    @Singleton
    public ConnectionSpec provideSpec () {
        return new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .supportsTlsExtensions(true)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
                .build();
    }
}
