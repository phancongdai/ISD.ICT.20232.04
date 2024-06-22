package entity.response;

import common.exception.*;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private String vnp_BankCode;
    private String vnp_PayDate;
    private String vnp_TransactionNo;
    private String vnp_TmnCode;
    private String vnp_SecureHash;
    private String vnp_OrderInfo;
    private String vnp_TxnRef;
    private String vnp_Amount;
    private String vnp_CardType;
    private String vnp_TransactionStatus;
    private String vnp_BankTranNo;
    private String vnp_ResponseCode;

    public Response(String query) {
        Map<String, String> parseQuery = parseQueryString(query);
        this.vnp_ResponseCode = parseQuery.get("vnp_ResponseCode");
        handleException();

        this.vnp_BankCode = parseQuery.get("vnp_BankCode");
        this.vnp_PayDate = parseQuery.get("vnp_PayDate");
        this.vnp_TransactionNo = parseQuery.get("vnp_TransactionNo");
        this.vnp_TmnCode = parseQuery.get("vnp_TmnCode");
        this.vnp_SecureHash = parseQuery.get("vnp_SecureHash");
        this.vnp_OrderInfo = parseQuery.get("vnp_OrderInfo");
        this.vnp_TxnRef = parseQuery.get("vnp_TxnRef");
        this.vnp_Amount = parseQuery.get("vnp_Amount");
        this.vnp_CardType = parseQuery.get("vnp_CardType");
        this.vnp_TransactionStatus = parseQuery.get("vnp_TransactionStatus");
        this.vnp_BankTranNo = parseQuery.get("vnp_BankTranNo");
    }

    public String getVnp_BankCode() {
        return vnp_BankCode;
    }

    public String getVnp_PayDate() {
        return vnp_PayDate;
    }

    public String getVnp_TransactionNo() {
        return vnp_TransactionNo;
    }

    public String getVnp_TmnCode() {
        return vnp_TmnCode;
    }

    public String getVnp_SecureHash() {
        return vnp_SecureHash;
    }

    public String getVnp_OrderInfo() {
        return vnp_OrderInfo;
    }

    public String getVnp_TxnRef() {
        return vnp_TxnRef;
    }

    public String getVnp_Amount() {
        return vnp_Amount;
    }

    public String getVnp_CardType() {
        return vnp_CardType;
    }

    public String getVnp_TransactionStatus() {
        return vnp_TransactionStatus;
    }

    public String getVnp_BankTranNo() {
        return vnp_BankTranNo;
    }

    public String getVnp_ResponseCode() {
        return vnp_ResponseCode;
    }

    private Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }

    private void handleException() throws TransactionNotDoneException, TransactionFailedException, TransactionReverseException, UnrecognizedException {
        //System.out.println(vnp_ResponseCode);
        switch (this.vnp_ResponseCode) {
            case "00":
                break;
            case "01":
                throw new TransactionNotDoneException();
            case "02":
                throw new TransactionFailedException();
            case "04":
                throw new TransactionReverseException();
            case "05":
                throw new ProcessingException();
            case "09":
                throw new RejectedTransactionException();
            case "06":
                throw new SendToBankException();
            case "07":
                throw new AnonymousTransactionException();
            case "24":
                System.out.println("ERROR: Giao dịch không thành công do: Khách hàng hủy giao dịch");
                break;
            case "11":
                System.out.println("ERROR: Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.");
                break;
            case "12":
                System.out.println("ERROR: Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.");
                break;
            case "13":
                System.out.println("ERROR: Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.");
                break;
            case "51":
                System.out.println("ERROR: Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.");
            case "65":
                System.out.println("ERROR: Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.");
                break;
            case "75":
                System.out.println("ERROR: Ngân hàng thanh toán đang bảo trì.");
                break;
            case "79":
                System.out.println("ERROR: Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch");
                break;
            case "99":
                System.out.println("ERROR: \tCác lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê)");
            default:
                throw new UnrecognizedException();
        }
    }

}
