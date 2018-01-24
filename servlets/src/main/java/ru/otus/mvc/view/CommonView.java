package ru.otus.mvc.view;

interface CommonView {
    String getView();
    void setStatus(int status);
    int getStatus();
    void setMessage(String message);
}
