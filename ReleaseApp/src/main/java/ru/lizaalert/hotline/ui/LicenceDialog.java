package ru.lizaalert.hotline.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.lizaalert.hotline.R;

/**
 * Created by defuera on 19/11/14.
 */
public class LicenceDialog {

    private static final String LOG_TAG = LicenceDialog.class.getSimpleName();

    public void show(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.licence);

        builder.setMessage(readFileFromAssets(context, "license.txt"));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private String readFileFromAssets(Context context, String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine = reader.readLine();
            String text = "";
            while (mLine != null) {
                text += mLine;
                mLine = reader.readLine();
            }
            Log.i(LOG_TAG, "text " + text);
            return text;
        } catch (IOException e) {
            Log.e(LOG_TAG, "couldn't open license.txt file");
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                    Log.w(LOG_TAG, "fail to close input stream");
                }
            }
        }
        return null;
    }
}
