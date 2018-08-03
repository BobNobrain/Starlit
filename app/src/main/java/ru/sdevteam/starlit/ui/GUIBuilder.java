package ru.sdevteam.starlit.ui;

import android.graphics.Color;

import ru.sdevteam.starlit.core.GameObject;
import ru.sdevteam.starlit.ui.components.AnimatedBackground;
import ru.sdevteam.starlit.ui.components.SolidBorder;
import ru.sdevteam.starlit.ui.components.TextLabel;

public class GUIBuilder {
    public static GameObject createButton(
            GameObject source,
            String label
    ) {
        if (source == null) source = new GameObject();

        AnimatedBackground bg = new AnimatedBackground();
        source.attachComponent(bg);

        if (label != null) {
            TextLabel text = new TextLabel(label, Color.WHITE);
            text.textSize = 20;
            source.attachComponent(text);
        }

        SolidBorder border = new SolidBorder(Color.WHITE, 1F);
        source.attachComponent(border);

        return source;
    }
}
