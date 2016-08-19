package brotherjing.com.stepcountmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://report-segstep.51yund.com/sport/report_runner_info_step_batch";
    private static final String URL_POST_WECHAT = "http://api.51yund.com/upload_step_info_wx/";
    private static final String KEY_LAST_TIMESTAMP = "last_timestamp";

    private EditText etStep;
    private TextView tvLastTime, tvLog;
    private Button btnGenHash, btnUpload, btnPostWechat;

    private String lastTimestamp;
    private StringBuilder stringBuilder = new StringBuilder();

    HashMap<String,String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastTimestamp = SharedPrefUtil.getString(this, KEY_LAST_TIMESTAMP, Config.INIT_TIMESTAMP);

        tvLog = (TextView)findViewById(R.id.tv_log);
        tvLastTime = (TextView)findViewById(R.id.tv_last_time);
        etStep = (EditText)findViewById(R.id.et_step);
        btnGenHash = (Button)findViewById(R.id.btn_generate_hash);
        btnUpload = (Button)findViewById(R.id.btn_upload);
        btnPostWechat = (Button)findViewById(R.id.btn_post_wechat);

        tvLastTime.setText(lastTimestamp);

        btnGenHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(etStep.getText()))return;

                Long newTime = (System.currentTimeMillis()/1000);
                Long timeConsume = newTime - (Long.parseLong(lastTimestamp));

                params.put("user_id", Config.USER_ID);
                params.put("kind_id", "2");
                params.put("subtype", "0");
                params.put("steps_array_json", "[{\"run_ts\":"+newTime+",\"cost_time\":"+timeConsume+",\"index\":0,\"step\":"+etStep.getText().toString()+"}]");
                params.put("ver", Config.VERSION);
                params.put("source", "android_app");
                params.put("os", Config.OS_VERSION);
                params.put("sdk", Config.SDK);
                params.put("device_id", Config.DEVICE_ID);
                params.put("client_user_id", Config.USER_ID);
                params.put("channel", Config.CHANNEL);
                params.put("phone_type", Config.PHONE_TYPE);
                params.put("source", "android_app");
                String sign = OpenSign.genSign(URL, params);
                params.put("sign", sign);
                params.put("xyy", Config.XYY);
                /*params.put("user_id","128829342");
                params.put("kind_id","2");
                params.put("subtype","0");
                params.put("steps_array_json","[{\"run_ts\":"+newTime+",\"cost_time\":"+timeConsume+",\"index\":0,\"step\":"+etStep.getText().toString()+"}]");
                params.put("ver", "3.1.2.8.488");
                params.put("source", "android_app");
                params.put("os", "5.1.1");
                params.put("sdk", "22");
                params.put("device_id", "867830026122160");
                params.put("client_user_id", "3181341");
                params.put("channel", "channel_xiaomi");
                params.put("phone_type", "Mi-4c");
                params.put("source", "android_app");
                String sign = OpenSign.genSign(URL,params);
                params.put("sign",sign);
                params.put("xyy","buw7zem3lvvdit0rc29oh4sk8");*/

                log("new time: "+newTime);
                log("time consumed: "+timeConsume);
                log(sign);

                tvLastTime.setText(newTime+"");
                lastTimestamp = newTime+"";
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpClient.asyncPostForm(URL, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        log("failed: "+e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        log(response.body().string());
                        SharedPrefUtil.putString(MainActivity.this, KEY_LAST_TIMESTAMP, lastTimestamp+"");
                    }
                });
            }
        });

        btnPostWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HttpClient.asyncPostForm(URL_POST_WECHAT, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        log("failed: "+e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        log(response.body().string());
                    }
                });
            }
        });
    }

    private void log(String log){
        Log.i("yj",log);
        stringBuilder.append(log).append("\n");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLog.setText(stringBuilder.toString());
            }
        });
    }
}
