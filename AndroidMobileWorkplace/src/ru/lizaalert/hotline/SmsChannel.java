/*
    Copyright (c) 2014 Anton Prozorov
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

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.Calendar;

public class SmsChannel extends Channel {

    private static final int DELIVERY_TIMEOUT = 1000 * 60 * 10; // 10 min delivery timeout
    private Thread deliveryThread;
    private BroadcastReceiver smsStatusReceiver;

    private ArrayList<String> messageParts = new ArrayList<String>();

    public SmsChannel(Context context) {
        super(context);
    }

    @Override
    public void send(String text, final String destination, final ChannelHandler channelHandler) {
        final SmsManager smsManager = SmsManager.getDefault();

        final PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0 , new Intent(IntentFields.SMS_SENT), 0);
        final PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent(IntentFields.SMS_DELIVERED), 0);

        deliveryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(DELIVERY_TIMEOUT);
                    context.unregisterReceiver(smsStatusReceiver);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        deliveryThread.start();

        smsStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (IntentFields.SMS_SENT.equals(action)) {
                    channelHandler.sent(Calendar.getInstance());
                }

                if (IntentFields.SMS_DELIVERED.equals(action)) {
                    channelHandler.delivered(Calendar.getInstance());
                    context.unregisterReceiver(this);
                    deliveryThread.interrupt();
                }
            }
        };

        IntentFilter smsStatusIntentFilter = new IntentFilter();
        smsStatusIntentFilter.addAction(IntentFields.SMS_SENT);
        smsStatusIntentFilter.addAction(IntentFields.SMS_DELIVERED);

        context.registerReceiver(smsStatusReceiver, smsStatusIntentFilter);

        messageParts = smsManager.divideMessage(text);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (messageParts.size() == 1) {
                    smsManager.sendTextMessage(destination, null, messageParts.get(0), sentIntent, deliveredIntent);
                } else if (messageParts.size() > 1) {
                    ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
                    sentIntents.add(sentIntent);

                    ArrayList<PendingIntent> deliveredIntents = new ArrayList<PendingIntent>();
                    deliveredIntents.add(deliveredIntent);

                    smsManager.sendMultipartTextMessage(destination, null, messageParts, sentIntents, deliveredIntents);
                }
            }
        }).start();
    }
}
