package vn.com.hoankiem360.utils;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Binh on 13-Sep-17.
 */

public class PostStringRequest extends StringRequest {
    public static final String TAG = PostStringRequest.class.getSimpleName();
    private Map<String, String> params;

    public PostStringRequest(String[] dataNames,
                             String[] data,
                             String link,
                             Response.Listener<String> listener) {
        super(Method.POST, link, listener, null);
        if (dataNames.length != data.length) {
            throw new IllegalArgumentException("data.length = " + data.length
                            + "\n" + "dataNames.length" + dataNames.length);
        }
        params = new HashMap<>();
        for (int i = 0; i < dataNames.length; i++) {
            params.put(dataNames[i], data[i]);
        }
        setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10,
                3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getParams() {
        Log.d(TAG, "getParams: params = " + params.toString());
        return params;
    }
}
