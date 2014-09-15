package ru.lizaalert.hotline;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import java.util.Calendar;

/**
 * Created by Anton Prozorov on 15.09.14.
 */
public class SmsChannel extends Channel {

    public SmsChannel(Context context) {
        super(context);
    }

    @Override
    public void send(final String text, final String destination, final ChannelHandler channelHandler) {
        final SmsManager smsManager = SmsManager.getDefault();

        final PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0 , new Intent(IntentFields.SMS_SENT), 0);
        final PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent(IntentFields.SMS_DELIVERED), 0);

        BroadcastReceiver smsStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (IntentFields.SMS_SENT.equals(action)) {
                    channelHandler.sent(Calendar.getInstance());
                }

                if (IntentFields.SMS_DELIVERED.equals(action)) {
                    channelHandler.delivered(Calendar.getInstance());
                }
                context.unregisterReceiver(this);
            }
        };

        IntentFilter smsStatusIntentFilter = new IntentFilter();
        smsStatusIntentFilter.addAction(IntentFields.SMS_SENT);
        smsStatusIntentFilter.addAction(IntentFields.SMS_DELIVERED);

        context.registerReceiver(smsStatusReceiver, smsStatusIntentFilter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                smsManager.sendTextMessage(destination, null, text, sentIntent, deliveredIntent);
            }
        }).start();
    }
}
