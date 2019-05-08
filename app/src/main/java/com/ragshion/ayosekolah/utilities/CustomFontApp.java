package com.ragshion.ayosekolah.utilities;

import android.app.Application;
import com.ragshion.ayosekolah.utilities.FontsOverride;

public class CustomFontApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "font/Muli.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font/Muli.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "font/Muli.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "font/Muli.ttf");
    }
}
