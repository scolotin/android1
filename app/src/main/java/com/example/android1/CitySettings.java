package com.example.android1;

public class CitySettings {
    private static boolean isCheckedHumidity, isCheckedWindSpeed;

    private static CitySettings instance = null;

    private static final Object SYNC_OBJ = new Object();

    private CitySettings() {
        isCheckedHumidity = false;
        isCheckedWindSpeed = false;
    }

    public static CitySettings getInstance() {
        synchronized (SYNC_OBJ) {
            if (instance == null) {
                instance = new CitySettings();
            }
            return instance;
        }
    }

    public boolean isCheckedHumidity() {
        return isCheckedHumidity;
    }

    public void setCheckedHumidity(boolean checkedHumidity) {
        isCheckedHumidity = checkedHumidity;
    }

    public boolean isCheckedWindSpeed() {
        return isCheckedWindSpeed;
    }

    public void setCheckedWindSpeed(boolean checkedWindSpeed) {
        isCheckedWindSpeed = checkedWindSpeed;
    }
}
