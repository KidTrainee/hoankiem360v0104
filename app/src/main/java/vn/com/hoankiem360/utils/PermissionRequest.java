package vn.com.hoankiem360.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import vn.com.hoankiem360.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@TargetApi(Build.VERSION_CODES.M)
public class PermissionRequest {
    private static final String TAG = "PermissionRequest";
    /**
     * Generic permission code used by this class.
     */
    private static AtomicInteger mRequestId = new AtomicInteger(0);

    private final Activity mActivity;
    private final ViewGroup mLayout;
    private final int mRationaleId;
    private final int mGrantedId;
    private final int mDeniedId;
    private final String[] mPermissions;
    private final Callback mCallback;
    private int mRequestCode;

    private PermissionRequest(Builder builder){
        mActivity = builder.mActivity;
        mLayout = builder.mLayout;
        mRationaleId = builder.mRationaleId;
        mGrantedId = builder.mGrantedId;
        mDeniedId = builder.mDeniedId;
        mPermissions = builder.mPermissions;
        mCallback = builder.mCallback;
    }

    public static boolean verifyPermissions(int... grantResults){
        // At least one result must be checked.
        if(grantResults.length < 1){
            return false;
        }
        // Verify that each required permission has been granted,
        // otherwise return false.
        for (int result : grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    /**
     * Set the {@code activity} target for this permission request and returns
     * a fluent Builder object for setting additional request attributes.
     * The activity is required to either extend PermissionActivity or override
     * ContextCompat#onRequestPermissionResults() and redirect the call to
     * PermissionRequest.onRequestPermissionResults().
     */
    @NonNull
    public static Builder with(@NonNull Activity acitity){
        Log.d(TAG, "with: creating PermissionRequest.Builder");
        return new Builder(acitity);
    }

    private PermissionRequest submit(){
        int showRationale = 0;
        ArrayList<String> requests = new ArrayList<>();
        for(final String permission : mPermissions){
            Log.d(TAG, "PermissionRequest.submit: permission="+permission);
            if(mActivity.checkSelfPermission(permission) !=
                    PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "PermissionRequest.submit: permission != granted");
                requests.add(permission);
                if(ActivityCompat.shouldShowRequestPermissionRationale(
                        mActivity, permission
                )) {
                    Log.d(TAG, "PermissionRequest.submit: "
                            + permission
                            + " shouldShowRationale");
                    showRationale++ ;
                }
            }
        }
        mRequestCode = mRequestId.addAndGet(1);
        // All the requests are already granted
        if(requests.isEmpty()) {
            // so immediately call the callback handler.

            if (mCallback != null) {
                Log.d(TAG, "PermissionRequest.submit: callback != null");
                mCallback.onPermissionGranted();
            }
        } else if (showRationale != 0){
            // Do sth when user denies permissions first time.
        } else {
            // Provide and additional rationale to the user if the permission
            // was not granted and the user would benefit from additional
            // context for the use of the permission. For example if the
            // user has previously denied the permission.
            Log.d(TAG, "PermissionRequest.submit: showRationale= "+showRationale);
            showRationale();
        }
        return this;
    }
    /**
     * Convenience method that shows an about dialog for this application.
     */
    private void showRationale(){
        Log.d(TAG, "PermissionRequest.showRationale: still working");
        String msg;
        // If the default rationale is set, then add the list of requested permissions
        // as a format argument. Note that this is just the default behaviour and apps
        // should design and set their own meaningful rationales.
        Log.d(TAG, "PermissionRequest.showRationale: mRationaleId = " + mRationaleId);
        if(mRationaleId == R.string.permission_rationale){
            String permissionStrings = "";
            for(final String permission : mPermissions){
                permissionStrings += "\n"+permission;
            }
            Log.d(TAG, "PermissionRequest.showRationale: permissionStrings = "+permissionStrings);
            msg = String.format(mActivity.getString(R.string.permission_rationale),
                    permissionStrings);
        } else {
            // App supplied rationale.
            msg = mActivity.getString(mRationaleId);
        }
        if(mLayout!=null){
            Snackbar snackbar = Snackbar.make(mLayout, msg, Snackbar.LENGTH_INDEFINITE);
            Log.d(TAG, "PermissionRequest.showRationale: mLayout != null");
            snackbar.setAction(R.string.permissions_ok_button, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(mActivity, mPermissions, mRequestCode);
                }
            });

            customSnackbarLayout(snackbar);
            snackbar.show();
        } else {
            Log.d(TAG, "PermissionRequest.showRationale: mLayout == null");
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
            // Set title and message.
            alertDialogBuilder.setTitle("Permission Request")
                    .setMessage(msg)
                    .setCancelable(true)
                    .setPositiveButton(R.string.permissions_ok_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(
                                    mActivity, mPermissions, mRequestCode
                            );
                        }
                    })
                    .create()
                    .show();
        }
    }

    /**
     * Helper method to custom the snackbar layout by using Snackbar.getView() method
     * Initially, snackbar layout is a horizontal LinearLayout object whose children
     * are a TextView and a Button.
     * @param snack
     */
    private void customSnackbarLayout(Snackbar snack){
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snack.getView();
        // Hide the default textview.
        TextView text = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        text.setSingleLine(false);


//        LayoutInflater inflater = LayoutInflater.from(layout.getContext());
//        View view = inflater.inflate(R.layout.my_snackbar, )
    }

    /**
     * Uses either Snackbar or Toast depending on snackbar setting.
     */
    private void showMessage(@StringRes int id){
        if(mLayout != null){
            Snackbar.make(mLayout, id, Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity, id, Toast.LENGTH_SHORT).show();
        }
    }

    public static AtomicInteger getRequestId() {
        return mRequestId;
    }

    /**
     *  Called from PermissionActivity to process the results of a permission
     *  request. Activities using this class that do not extend the
     *  PermissionActivity are required to override
     *  ContextCOmpat#onRequestPermissionResults() and redirect the call
     *  to this method.
     */
    public boolean onRequestPermissionResult(int requestCde,
                                                @NonNull String[] permissions,
                                                @NonNull int[] grantResults){
        // display the grant or denial of this permission request via and
        // unobstrusive toast message.
        if(verifyPermissions(grantResults)){
            // show granted message
            showMessage(mGrantedId);
            if(mCallback!=null){
                mCallback.onPermissionGranted();
            }
        } else {
            showMessage(mDeniedId);
            if(mCallback!=null){
                mCallback.onPermissionDenied();
            }
        }
        return true;
    }



    /**
     * Callback invoked when the permission request completes.
     */
    public interface Callback{
        void onPermissionGranted();
        void onPermissionDenied();
    }
    /**
     * (@code PermissionRequest) builder static inner class
     */
    public static final class Builder{
        private  Activity mActivity;
        private  ViewGroup mLayout;
        private  int mRationaleId;
        private  int mGrantedId;
        private  int mDeniedId;
        private  String[] mPermissions;
        private  Callback mCallback;

        private Builder (Activity activity){
            Log.d(TAG, "Builder: in PermissionRequest.Builder's constructor");
            mActivity = activity;}
        /**
         * Sets a layout used for Snackbar display. If this value is not set,
         * the permission rationale requests will use a dialog and results will
         * be posted in a Toast message.
         */
        @NonNull
        public Builder snackbar(@NonNull ViewGroup layout){
            if(mLayout!=null){
                throw new IllegalArgumentException(
                        "A snackbar layout has already been set."
                );
            }
            mLayout = layout;
            Log.d(TAG, "PermissionRequest.Builder.snackbar: layout = "+mLayout);
            return this;
        }
        /**
         * Sets a string resource that will be displayed in a SnackBar when requesting
         * the permissions
         */
        @NonNull
        public Builder rationale(@StringRes int id){
            if(mRationaleId != 0){
                throw new IllegalArgumentException(
                        "A rationale string resource has already been set."
                );
            }
            mRationaleId = id;
            Log.d(TAG, "PermissionRequest.Builder.rationale: "+mRationaleId);
            return this;
        }
        /**
         * Sets a string resource that will be displayed in a Snackbar
         * when the requested permissions are all granted.
         */
        @NonNull
        public Builder granted(@StringRes int id){
            if(mGrantedId != 0){
                throw new IllegalArgumentException(
                        "A granted string resource has already been set."
                );
            }
            mGrantedId = id;
            Log.d(TAG, "PermissionRequest.Builder.granted: "+mGrantedId);
            return this;
        }
        /**
         * Sets a string resource that will be displayed in a Snackbar when any of the
         * requested permissions is denied.
         */
        @NonNull
        public Builder denied(@StringRes int id){
            if(mDeniedId!=0){
                throw new IllegalArgumentException(
                        "A denied string resource has already been set."
                );
            }
            mDeniedId = id;
            Log.d(TAG, "PermissionRequest.Builder.denied: "+mDeniedId);
            return this;
        }
        /**
         * Set permissions that will be requested.
         */
        @NonNull
        public Builder permissions(@NonNull String... permissions){
            if(mPermissions != null){
                throw new IllegalArgumentException(
                        "Permissions have already been set."
                );
            }
            mPermissions = permissions;
            Log.d(TAG, "PermissionRequest.Builder.permissions: "+mPermissions);
            return this;
        }
        /**
         * Sets a Callback that will be used to notify the results of the
         * permission request.
         */
        @NonNull
        public Builder callback(@NonNull Callback callback){
            if(mCallback!=null){
                throw new IllegalArgumentException(
                        "A callback has already been set."
                );
            }
            mCallback = callback;
            Log.d(TAG, "PermissionRequest.Builder.callback: "+mCallback);
            return this;
        }

        /**
         * Returns a {@code PermissionRequest} built from the parameters previously set.
         */
        @NonNull
        public PermissionRequest submit(){
            // Validate required fiels.
            if(mActivity==null){
                throw new NullPointerException("An activity muast be set.");
            }
            if(mPermissions==null){
                throw new NullPointerException("Permissions must be set.");
            }
            if(mRationaleId==0){
                Log.w(TAG, "Default rationale should only be used during development");
                mRationaleId = R.string.permission_rationale;
            }
            if(mGrantedId==0){
                mGrantedId = R.string.permissions_granted;
            }
            if(mDeniedId==0){
                mDeniedId = R.string.permissions_denied;
            }
            Log.d(TAG, "PermissionRequest.Builder.submit: still work here");
            return new PermissionRequest(this).submit();
        }
    }
}
