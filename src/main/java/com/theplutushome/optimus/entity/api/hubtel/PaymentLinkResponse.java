package com.theplutushome.optimus.entity.api.hubtel;
//{
//        "responseCode": "0000",
//        "status": "Success",
//        "data": {
//        "checkoutUrl": "https://pay.hubtel.com/79e2aa6e9bb148e1aa33cfb53ee852c0",
//        "checkoutId": "79e2aa6e9bb148e1aa33cfb53ee852c0",
//        "clientReference": "inv001",
//        "message": "",
//        "checkoutDirectUrl": "https://pay.hubtel.com/79e2aa6e9bb148e1aa33cfb53ee852c0/direct"
//        }
//        }
public class PaymentLinkResponse {
    private String responseCode;
    private String status;
    private ResultData data;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }

    public static class ResultData {
        private String checkoutUrl;
        private String checkoutId;
        private String clientReference;
        private String message;
        private String checkoutDirectUrl;

        public String getCheckoutUrl() {
            return checkoutUrl;
        }

        public void setCheckoutUrl(String checkoutUrl) {
            this.checkoutUrl = checkoutUrl;
        }

        public String getCheckoutId() {
            return checkoutId;
        }

        public void setCheckoutId(String checkoutId) {
            this.checkoutId = checkoutId;
        }

        public String getClientReference() {
            return clientReference;
        }

        public void setClientReference(String clientReference) {
            this.clientReference = clientReference;
        }

        public String getCheckoutDirectUrl() {
            return checkoutDirectUrl;
        }

        public void setCheckoutDirectUrl(String checkoutDirectUrl) {
            this.checkoutDirectUrl = checkoutDirectUrl;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
