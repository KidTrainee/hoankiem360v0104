package vn.com.hoankiem360.requests;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Binh on 27-Sep-17.
 */

public class GetStringRequest extends StringRequest {
    public GetStringRequest(String url, Response.Listener<String> listener) {
        super(Method.GET, url, listener, null);

        setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
