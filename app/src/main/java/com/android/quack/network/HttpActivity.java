package com.android.quack.network;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.quack.R;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;

public class HttpActivity extends Activity {

    private static final String TAG = "Http";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
    }

    public void onHttpUrlConnectionClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.baidu.com");

                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setRequestMethod("GET");
                    // 开始连接
                    urlConn.connect();
                    // 判断请求是否成功
                    if (urlConn.getResponseCode() == 200) {
                        // 获取返回的数据
                        String result = streamToString(urlConn.getInputStream());
                        Log.d(TAG, "Get方式请求成功，result--->" + result);
                    } else {
                        Log.e(TAG, "Get方式请求失败");
                    }
                    // 关闭连接
                    urlConn.disconnect();
                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }).start();
    }

    public void onHttpClientClick(View view) {
        Toast.makeText(this, "Android API 23已废弃HttpClient", Toast.LENGTH_SHORT).show();
    }

    public void onOkHttpClick(View view) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.code() + "");
                    Log.d(TAG, response.message());
                    Log.d(TAG, response.body().string());
                }
            }
        });
    }

    public void onRetrofitClick(View view) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.baidu.com")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            HtmlService htmlService = retrofit.create(HtmlService.class);

            Call<String> call = htmlService.getHtml();

            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, response.code() + "");
                        Log.d(TAG, response.message());
                        Log.d(TAG, response.body());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void onVolleyClick(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest("https://www.baidu.com",
            new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                }
            },
            new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
        queue.add(stringRequest);
    }

    public void onAndroidAsyncHttpClick(View view) {
        Toast.makeText(this, "android-async-http基于HttpClient已不维护", Toast.LENGTH_SHORT).show();
    }

    private static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public interface HtmlService {
        @GET("/")
        Call<String> getHtml();
    }
}
