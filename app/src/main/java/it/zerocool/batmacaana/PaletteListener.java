package it.zerocool.batmacaana;

import android.support.v7.graphics.Palette;

import java.lang.ref.WeakReference;

class PaletteListener implements Palette.PaletteAsyncListener {
    private final WeakReference<PlaceFragment> activityWeakReference;

    public static PaletteListener newInstance(PlaceFragment activity) {
        WeakReference<PlaceFragment> activityWeakReference = new WeakReference<PlaceFragment>(activity);
        return new PaletteListener(activityWeakReference);
    }

    PaletteListener(WeakReference<PlaceFragment> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    @Override
    public void onGenerated(Palette palette) {
        PlaceFragment activity = activityWeakReference.get();
        if (activity != null) {
            activity.setPalette(palette);
        }
    }
}