package com.luvsoft.statistic;

public class Types {
    public static class StatRecord{
        public int quantity;
        public double amount;

        public StatRecord(){
            quantity = 0;
            amount = 0.0d;
        }
    }

    public enum CouponType{
        Coupon_Buy(1),              // Phiếu nhập mua
        Coupon_Customer_Return(2),  // Phiếu nhập hàng trả lại
        Coupon_Return_Provider(3);  // Phiếu xuất trả nhà cung cấp

        private final Integer num;
        private CouponType(int index){
            num = index;
        }
        
        public Integer getValue(){ return num;}
    };

}
