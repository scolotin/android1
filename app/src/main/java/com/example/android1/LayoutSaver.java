package com.example.android1;

public class LayoutSaver {
    private int layoutId;

    private static LayoutSaver instance = null;

    private static final Object SYNC_OBJ = new Object();

    private LayoutSaver() {
    }

    public static LayoutSaver getInstance() {
        synchronized (SYNC_OBJ) {
            if (instance == null) {
                instance = new LayoutSaver();
            }
            return instance;
        }
    }

    public int getId() {
        return layoutId;
    }

    public void saveId(int layoutId) {
        this.layoutId = layoutId;
    }
}
