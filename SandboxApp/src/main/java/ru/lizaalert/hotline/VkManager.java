/*
    Copyright (c) 2014 Denis Volyntsev <fortun777@gmail.com>
    Copyright (c) 2014 Other contributors as noted in the AUTHORS file.

    Этот файл является частью приложения "Мобильное рабочее место оператора
    Горячей линии по пропавшим детям".

    Данная лицензия разрешает лицам, получившим копию "Мобильного рабочего
    места оператора Горячей линии по пропавшим детям" и сопутствующей
    документации (в дальнейшем именуемыми «Программное Обеспечение»),
    безвозмездно использовать Программное Обеспечение без ограничений, включая
    неограниченное право на использование, копирование, изменение, добавление,
    публикацию, распространение, сублицензирование и/или продажу копий
    Программного Обеспечения, также как и лицам, которым предоставляется данное
    Программное Обеспечение, при соблюдении следующих условий:

    Указанное выше уведомление об авторском праве и данные условия должны быть
    включены во все копии или значимые части данного Программного Обеспечения.

    ДАННОЕ ПРОГРАММНОЕ ОБЕСПЕЧЕНИЕ ПРЕДОСТАВЛЯЕТСЯ «КАК ЕСТЬ», БЕЗ КАКИХ-ЛИБО
    ГАРАНТИЙ, ЯВНО ВЫРАЖЕННЫХ ИЛИ ПОДРАЗУМЕВАЕМЫХ, ВКЛЮЧАЯ, НО НЕ
    ОГРАНИЧИВАЯСЬ ГАРАНТИЯМИ ТОВАРНОЙ ПРИГОДНОСТИ, СООТВЕТСТВИЯ ПО ЕГО
    КОНКРЕТНОМУ НАЗНАЧЕНИЮ И ОТСУТСТВИЯ НАРУШЕНИЙ ПРАВ. НИ В КАКОМ СЛУЧАЕ
    АВТОРЫ ИЛИ ПРАВООБЛАДАТЕЛИ НЕ НЕСУТ ОТВЕТСТВЕННОСТИ ПО ИСКАМ О ВОЗМЕЩЕНИИ
    УЩЕРБА, УБЫТКОВ ИЛИ ДРУГИХ ТРЕБОВАНИЙ ПО ДЕЙСТВУЮЩИМ КОНТРАКТАМ, ДЕЛИКТАМ
    ИЛИ ИНОМУ, ВОЗНИКШИМ ИЗ, ИМЕЮЩИМ ПРИЧИНОЙ ИЛИ СВЯЗАННЫМ С ПРОГРАММНЫМ
    ОБЕСПЕЧЕНИЕМ ИЛИ ИСПОЛЬЗОВАНИЕМ ПРОГРАММНОГО ОБЕСПЕЧЕНИЯ ИЛИ ИНЫМИ
    ДЕЙСТВИЯМИ С ПРОГРАММНЫМ ОБЕСПЕЧЕНИЕМ.

    Кроме содержимого в этом уведомлении, ни название "Горячей линии по
    пропавшим детям", ни название "Добровольного поискового отряда "Лиза
    Алерт", ни имена вышеуказанных держателей авторских прав не должно быть
    использовано в рекламе или иным способом, чтобы увеличивать продажу,
    использование или другие работы в этом Программном обеспечении без
    предшествующего письменного разрешения.

    Permission is hereby granted, free of charge, to any person obtaining a
    copy of this software and associated documentation files (the "Software"),
    to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the
    Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
    THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
    DEALINGS IN THE SOFTWARE.

    Except as contained in this notice, the name of Liza Alert or the name of
    Liza Alerts's hotline department or the name(s) the above copyright holders
    shall not be used in advertising or otherwise to promote the sale, use or
    other dealings in this Software without prior written authorization.
 */

package ru.lizaalert.hotline;

import ru.lizaalert.hotline.sandbox.R;

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
        VKSdk.initialize(vkSdkListener, context.getString(R.string.vk_app_id));
        VKSdk sdk = VKSdk.instance();
        sdk.setSdkListener(vkSdkListener);
    }

    private boolean loginIfNeeded() {
        boolean isLoggedIn = VKSdk.isLoggedIn();
        Log.d(TAG, "initVK is loggedIn ? " + isLoggedIn);

        if (!isLoggedIn)
            VKSdk.authorize(VK_PERMISSIONS_SCOPE);

        return isLoggedIn;
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
            Toast.makeText(context, R.string.login_vk_successful, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * https://vk.com/dev/wall.post
     */
    public void requestWallPost(String message) {
        if (loginIfNeeded()) {
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
                    Toast.makeText(context, R.string.post_successful, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(VKError error) {
                    String errorMsg = error.errorCode + ": " + error.errorMessage;
                    Log.i(TAG, "requestWallPost error: " + errorMsg);
                    if (error.errorCode == -101) {
                        showDialog();
                    } else {
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
