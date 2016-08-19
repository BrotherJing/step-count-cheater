package brotherjing.com.stepcountmaster;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class HttpClient {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client;
    private static final Gson gson = new Gson();

    public synchronized static OkHttpClient getHttpClient(){
        if(client==null){
            client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(new UserAgentInterceptor())
                    .build();
        }
        return client;
    }

    public static <T> T syncGet(URL url, Class<T> clazz)throws IOException{
        return syncGet(url.toString(),clazz);
    }

    public static <T> T syncGet(String url, Class<T> clazz) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = getHttpClient().newCall(request).execute();
        if(!response.isSuccessful())throw new IOException("unexpected http status: "+response);
        return gson.fromJson(response.body().charStream(),clazz);
    }

    public static Reader syncGetStream(String url)throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = getHttpClient().newCall(request).execute();
        if(!response.isSuccessful())throw new IOException("unexpected http status: "+response);
        return response.body().charStream();
    }

    public static <T> T syncPostJson(String url, String json, Class<T> clazz) throws IOException {
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = getHttpClient().newCall(request).execute();
        if(!response.isSuccessful())throw new IOException("unexpected http status: "+response);
        return gson.fromJson(response.body().charStream(),clazz);
    }

    public static void asyncGet(URL url, Callback callback){
        asyncGet(url.toString(), callback);
    }

    public static void asyncGet(String url, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        getHttpClient().newCall(request).enqueue(callback);
    }

    public static void asyncPostJson(String url, String json, Callback callback){
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        getHttpClient().newCall(request).enqueue(callback);
    }

    public static void asyncPostForm(String url, HashMap<String,String> params, Callback callback){
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,String> entry : params.entrySet()){
            builder.add(entry.getKey(),entry.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        getHttpClient().newCall(request).enqueue(callback);
    }

    private static final class UserAgentInterceptor implements Interceptor{
        private static final String HEADER_USER_AGENT = "User-Agent";
        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            final Request requestWithUA = request.newBuilder()
                    .removeHeader(HEADER_USER_AGENT)
                    .addHeader(HEADER_USER_AGENT,"okhttp/3.4.0")
                    .build();
            return chain.proceed(requestWithUA);
        }
    }
}
