package it.zerocool.batmacaana;

import android.support.v7.graphics.Palette;

import java.lang.ref.WeakReference;

class EventPaletteListener implements Palette.PaletteAsyncListener {
    private final WeakReference<EventFragment> activityWeakReference;

    public static EventPaletteListener newInstance(EventFragment activity) {
        WeakReference<EventFragment> activityWeakReference = new WeakReference<EventFragment>(activity);
        return new EventPaletteListener(activityWeakReference);
    }

    EventPaletteListener(WeakReference<EventFragment> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    @Override
    public void onGenerated(Palette palette) {
        EventFragment activity = activityWeakReference.get();
        if (activity != null) {
            activity.setPalette(palette);
        }
    }
}