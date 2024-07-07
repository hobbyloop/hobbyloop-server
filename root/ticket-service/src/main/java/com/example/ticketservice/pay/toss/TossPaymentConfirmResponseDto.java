package com.example.ticketservice.pay.toss;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class TossPaymentConfirmResponseDto {
    private String version;
    private String paymentKey;
    private String type;
    private String orderId;
    private String orderName;
    private String mId;
    private String currency;
    private String method;
    private Integer totalAmount;
    private Integer balanceAmount;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private Boolean useEscrow;
    private String lastTransactionKey;
    private Integer suppliedAmount;
    private Integer vat;
    private Boolean cultureExpense;
    private Integer taxFreeAmount;
    private Integer taxExemptAmount;
    private List<Cancel> cancels;
    private Card card;
    private VirtualAccount virtualAccount;
    private MobilePhone mobilePhone;
    private GiftCertificate giftCertificate;
    private Transfer transfer;
    private Receipt receipt;
    private Checkout checkout;
    private EasyPay easyPay;
    private String country;
    private TossFailureResponseDto failure;
    private CashReceipt cashReceipt;
    private List<CashReceipt> cashReceipts;
    private Discount discount;

    @Getter
    @ToString
    public static class Cancel {
        private Integer cancelAmount;
        private String cancelReason;
        private Integer taxFreeAmount;
        private Integer taxExemptionAmount;
        private Integer refundableAmount;
        private Integer easyPayDiscountAmount;
        private String canceledAt;
        private String transactionKey;
        private String receiptKey;
        private Boolean isPartialCancelable;
    }

    @Getter
    @ToString
    public static class Card {
        private Integer amount;
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private Integer installmentPlanMonths;
        private String approveNo;
        private Boolean useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private Boolean isInterestFree;
        private String interestPayer;
    }

    @Getter
    @ToString
    public static class VirtualAccount {
        private String accountType;
        private String accountNumber;
        private String bankCode;
        private String customerName;
        private String dueDate;
        private String refundStatus;
        private Boolean expired;
        private String settlementStatus;
        private RefundReceiveAccount refundReceiveAccount;
        private String secret;
    }

    @Getter
    @ToString
    public static class MobilePhone {
        private String customerMobilePhone;
        private String settlementStatus;
        private String receiptUrl;
    }

    @Getter
    @ToString
    public static class GiftCertificate {
        private String approveNo;
        private String settlementStatus;
    }

    @Getter
    @ToString
    public static class Transfer {
        private String bankCode;
        private String settlementStatus;
    }

    @Getter
    @ToString
    public static class Receipt {
        private String url;
    }

    @Getter
    @ToString
    public static class Checkout {
        private String url;
    }

    @Getter
    @ToString
    public static class EasyPay {
        private String provider;
        private Integer amount;
        private Integer discountAmount;
    }

    @Getter
    @ToString
    public static class TossFailureResponseDto {
        private String code;
        private String message;
    }

    @Getter
    @ToString
    public static class CashReceipt {
        private String type;
        private String receiptKey;
        private String issueNumber;
        private String receiptUrl;
        private Integer amount;
        private Integer taxFreeAmount;
    }

    @Getter
    @ToString
    public static class Discount {
        private Integer amount;
    }

    @Getter
    @ToString
    public static class RefundReceiveAccount {
        private String bankCode;
        private String accountNumber;
        private String holderName;
    }
}
