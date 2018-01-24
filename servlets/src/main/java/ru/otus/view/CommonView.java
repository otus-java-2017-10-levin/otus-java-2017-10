package ru.otus.view;

public interface CommonView {
    String getView();
    void setStatus(int status);
    int getStatus();
    void setMessage(String message);
    String getMessage();
}
