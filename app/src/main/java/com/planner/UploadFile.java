package com.planner;

public class UploadFile {
    private String fileName;
    private String fileUrl;

    public UploadFile() {

    }

    public UploadFile(String fileName, String fileUrl) {
        if (fileName.trim().equals("")) {
            fileName = "No file name";
        }
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }


}
