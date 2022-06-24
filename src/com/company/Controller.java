package com.company;

public class Controller {
    private View view;

    public void setView(View view) {
        this.view = view;
    }

    public void start() {
        view.create();
    }
}
