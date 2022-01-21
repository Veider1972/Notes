package ru.gb.notes.repository;

import ru.gb.notes.R;

public interface Constants {

    enum EditResult {
        RESULT_UPDATE, RESULT_DELETE, RESULT_CANCEL
    }

    int ITEMS_SPACE = 10;

    String PREFERENCES = "PREFERENCES";

    int[] images = {R.drawable.done, R.drawable.alarm, R.drawable.business, R.drawable.shopping};

    default int getImageById(int drawable_image) {
        for (int i = 0; i < images.length; i++) {
            if (drawable_image == images[i]) {
                return i;
            }
        }
        return 0;
    }
}