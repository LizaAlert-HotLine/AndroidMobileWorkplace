package ru.lizaalert.hotline;

import java.util.Calendar;

/**
 * Created by Anton Prozorov on 15.09.14.
 */
public interface ChannelHandler {
    void sent(Calendar c);
    void error(Calendar c, String message);
    void delivered(Calendar c);
}
