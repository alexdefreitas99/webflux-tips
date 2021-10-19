//package com.alex.springtips;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//
//public class StoreSaleRequest {
//    private StoreSaleDiscountType discountType;
//
//    private BigDecimal discountValue = BigDecimal.ZERO;
//
//    private BigDecimal discountPercentage = BigDecimal.ZERO;
//
//    public set
//
//    public BigDecimal getDiscountValue(BigDecimal amount) {
//        if (StoreSaleDiscountType.PERCENTAGE.equals(this.getDiscountType())) {
////returnDiscount desnecessario
//            return amount.multiply(getDiscountPercentage())
//                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
//        } else {
//            return discountValue;
//        }
//    }
//
//    public StoreSaleDiscountType getDiscountType() {
//        return discountType;
//    }
//
//    public void setDiscountType(StoreSaleDiscountType discountType) {
//        this.discountType = discountType;
//    }
//
//    public BigDecimal getDiscountValue() {
//        return discountValue;
//    }
//
//    public void setDiscountValue(BigDecimal discountValue) {
//        this.discountValue = discountValue;
//    }
//
//    public BigDecimal getDiscountPercentage() {
//        return discountPercentage;
//    }
//
//    public void setDiscountPercentage(BigDecimal discountPercentage) {
//        this.discountPercentage = discountPercentage;
//    }
//}