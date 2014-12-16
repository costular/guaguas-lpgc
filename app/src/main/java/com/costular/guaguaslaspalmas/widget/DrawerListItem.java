package com.costular.guaguaslaspalmas.widget;

/**
 * Created by Diego on 29/10/2014.
 */
public class DrawerListItem {

    private String title;
    private int icon;

    public DrawerListItem(final String title, final int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}
