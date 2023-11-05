package com.example.mobisi.model;

public interface OnImageUploadListener {
    void onUploadSuccess(String imageUri);
    void onUploadFailed();
}
