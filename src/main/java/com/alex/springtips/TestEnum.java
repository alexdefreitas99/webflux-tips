package com.alex.springtips;

public enum TestEnum {
    CASH(1),
    CREDIT_CARD(2),
    DEBIT_CARD(3),
    CHECK(4),
    BANK_SLIP(5),
    PAYMENT_BOOK(6),
    CREDIT_REBATE(7),
    BANK_DEPOSIT(8),
    PIX(9),
    BANK_TRANSFER(10),
    CASHBACK(11),
    DIGITAL_WALLET(12);

    private static final String ENUM_NAME = "TestEnum";

    private final Integer id;
    private final String description;

    TestEnum(Integer id) {
        this.id = id;
        this.description = this.name();
    }

    public static TestEnum toEnum(Integer id) {
        for (TestEnum x : TestEnum.values()) {
            if (x.getId().equals(id)) {
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid type id: " + id);
    }

    public static TestEnum toEnum(String description) {
        for (TestEnum x : TestEnum.values()) {
            if (x.getDescription().equalsIgnoreCase(description)) {
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + description);
    }

    public static String getEnumName() {
        return ENUM_NAME;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
