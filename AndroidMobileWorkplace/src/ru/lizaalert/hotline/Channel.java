package ru.lizaalert.hotline;

import android.content.Context;

/**
 * Created by Anton Prozorov on 15.09.14.
 */
public abstract class Channel {

    Context context;

    public Channel(Context context) {
        this.context = context;
    }

    public abstract void send(String text, String destination, ChannelHandler channelHandler);

}
