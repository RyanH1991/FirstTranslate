package com.example.firsttranslate;

import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.view.KeyEvent;

public class MediaButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }

        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }

        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            Intent translateIntent = new Intent("com.your.package.ACTION_TRANSLATE");
            context.sendBroadcast(translateIntent);
        }
    }
}
