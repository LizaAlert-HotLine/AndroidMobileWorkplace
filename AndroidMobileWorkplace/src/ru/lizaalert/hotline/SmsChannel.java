package ru.lizaalert.hotline;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Anton Prozorov on 15.09.14.
 */
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
