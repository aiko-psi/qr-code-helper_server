package de.aklingauf.qrcodehelper.payload;

import java.util.List;

public class QRListResponse {
    private Boolean success;
    private String message;
    private List<Long> qrCodeIdList;

    public QRListResponse(Boolean success, String message, List<Long> idList){
        this.success = success;
        this.message = message;
        this.qrCodeIdList = idList;

    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Long> getQrCodeIdList() {
        return qrCodeIdList;
    }

    public void setQrCodeIdList(List<Long> qrCodeIdList) {
        this.qrCodeIdList = qrCodeIdList;
    }
}
