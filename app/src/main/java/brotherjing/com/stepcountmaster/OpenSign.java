package brotherjing.com.stepcountmaster;

/**
 * Created by jingyanga on 2016/8/18.
 */

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class OpenSign {

    private static final String[] kSignExcept = new String[]{"nick", "content", "feeling", "alipay_realname", "sign", "xyy"};
    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final String UTF_8 = "UTF-8";

    public static String encodeUrl(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8").replace("+", "%20").replace("*", "%2A");
    }

    public static String makeSig(String method, String path, HashMap<String, String> hashMap, String signKey) throws Exception {
        Mac instance = Mac.getInstance(HMAC_ALGORITHM);
        instance.init(new SecretKeySpec(signKey.getBytes("UTF-8"), instance.getAlgorithm()));
        return Base64.encodeToString(instance.doFinal(makeSource(method, path, hashMap).getBytes("UTF-8")), 2);
    }

    public static String makeSource(String method, String path, HashMap<String, String> hashMap) throws UnsupportedEncodingException {
        Object[] toArray = hashMap.keySet().toArray();
        Arrays.sort(toArray);
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append(method.toUpperCase()).append("&").append(encodeUrl(path)).append("&");
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int i = 0; i < toArray.length; i++) {
            stringBuilder2.append(toArray[i]).append("=").append((String) hashMap.get(toArray[i]));
            if (i != toArray.length - 1) {
                stringBuilder2.append("&");
            }
        }
        stringBuilder.append(encodeUrl(stringBuilder2.toString()));
        return stringBuilder.toString();
    }

    public static String genSign(String str, HashMap<String,String> yDHttpParams) {
        HashMap hashMap = null;
        for (Object obj : kSignExcept) {
            if (yDHttpParams.containsKey(obj)) {
                if (hashMap == null) {
                    hashMap = new HashMap(yDHttpParams);
                }
                hashMap.remove(obj);
            }
        }
        if (hashMap != null) {
            HashMap hashMap2 = hashMap;
        }
        try {
            return OpenSign.makeSig("POST", getPathOfUrl(str), yDHttpParams, getSignkey());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getPathOfUrl(String str) {
        int indexOf;
        if (str.indexOf("http://") != -1) {
            indexOf = str.indexOf("/", 8);
        } else {
            indexOf = str.indexOf("/");
        }
        return str.substring(indexOf);
    }

    public static String getSignkey() {
        String str = "";
        /*try {
            str = Tools.getInstance().getUmengStrParams("signKey", "");
        } catch (Throwable th) {
        }*/
        if (str == "") {
            return "201505yuedongxiaoyue&";
        }
        return str;
    }
}
