package com.example.parktaeim.settingsui.Model;

/**
 * Created by parktaeim on 2018. 1. 25..
 */

public class SettingsRecyclerItem {
    private String title;
    private String connectText;

    public SettingsRecyclerItem(String title) {
        this.title = title;
    }

    public SettingsRecyclerItem(String title, String connectText) {
        this.title = title;
        this.connectText = connectText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConnectText() {
        return connectText;
    }

    public void setConnectText(String connectText) {
        this.connectText = connectText;
    }
}
