package ru.lizaalert.hotline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCaptchaDialog;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by defuera on 26/09/14.
 *
 * This manager handles vk authorisation and a wall post to Lisa Alert Group.
 *
 * Find vk application account here https://vk.com/editapp?id=4565821&section=info
 * WARNING for signed application or to test debug version on your machine you need to add fingerprints to vk application account.
 *
 * All the sdk documentation, including instructions of how to obtain fingerprints you can find here https://vk.com/dev/android_sdk;
 *
 * Api documentation https://vk.com/dev/methods
 */
public class VkManager {
    private static final String TAG = VkManager.class.getSimpleName();

    private static final String COMMUNITY_ID = "-77912698"; // minus is important here. When you want to post to a community wall "-" is nesessary
    private static final String VK_COMUNITY_URL = "https://vk.com/club77912698";
    private static final int MAX_ATTEMPTS = 10;

    private static VkManager instance;
    private Context context;

    public static VkManager getInstance(Context context) {
        if (instance == null)
            instance = new VkManager();
        instance.context = context;
        return instance;
    }

    /**
     * To receive necessary permissions during authorization, when the authorization window opens
     * you need to pass scope parameter containing names of the required permissions separated by space or comma.
     * https://vk.com/dev/permissions
     */
    public static final String VK_PERMISSIONS_SCOPE[] = {
            VKScope.WALL,
            VKScope.OFFLINE,
            VKScope.GROUPS,
    };

    public void initVk() {
        VKAccessToken token = VKAccessToken.tokenFromSharedPreferences(context, SettingsConsts.VK_TOKEN);
        VKSdk.initialize(vkSdkListener, context.getString(R.string.vk_app_id), token);

        VKSdk sdk = VKSdk.instance();
        sdk.setSdkListener(vkSdkListener);


        Log.d(TAG, "initVK is loggedIn ? " + VKSdk.isLoggedIn());
        if (!VKSdk.isLoggedIn())
            VKSdk.authorize(VK_PERMISSIONS_SCOPE);
    }

    private final VKSdkListener vkSdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(VK_PERMISSIONS_SCOPE);
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity()) // can't use context, get exception
                    .setMessage(authorizationError.errorMessage)
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            newToken.saveTokenToSharedPreferences(context, SettingsConsts.VK_TOKEN);
        }
    };

    /**
     * https://vk.com/dev/wall.post
     */
    public void requestWallPost(String message) {
        VKParameters parameters = new VKParameters(VKParameters.from(
                VKApiConst.OWNER_ID, COMMUNITY_ID,
                VKApiConst.MESSAGE, message
        ));
        VKRequest request = VKApi.wall().post(parameters);
        request.attempts = MAX_ATTEMPTS;

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.i(TAG, "resp: " + response.json);
                Toast.makeText(context, R.string.post_successfull, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VKError error) {
                String errorMsg = error.errorCode + ": " + error.errorMessage;
                Log.i(TAG, "requestWallPost error: " +errorMsg);
                if (error.errorCode == -101){
                    showDialog();
                } else {
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialog() { //TODO: check if user send a request already and change dialog message to, not still approved to comunity? wait little bit more.. or smthg..
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.vk_send_group_request_title);
        builder.setMessage(R.string.vk_send_group_request_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = VK_COMUNITY_URL;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.create().show();
    }
}
