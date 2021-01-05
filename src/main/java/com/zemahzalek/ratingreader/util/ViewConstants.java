package com.zemahzalek.ratingreader.util;

public class ViewConstants {

    public static final int EPISODE_ITEM_WIDTH = 350;
    public static final int EPISODE_ITEM_HEIGHT = 63;
    public static final int RESULT_SCROLLPANE_SCROLLBAR_WIDTH = 15;

    public static final int INITIAL_WIDTH = EPISODE_ITEM_WIDTH * 2;
    public static final int INITIAL_HEIGHT = 200 + EPISODE_ITEM_HEIGHT * 5;
    public static final int MIN_WIDTH = INITIAL_WIDTH;
    public static final int MIN_HEIGHT = INITIAL_HEIGHT;
    public static final int MAX_WIDTH = EPISODE_ITEM_WIDTH * 4;
    public static final int MAX_HEIGHT = 200 + EPISODE_ITEM_HEIGHT * 8;


    // Not instantiatable.
    private ViewConstants() {}
}
