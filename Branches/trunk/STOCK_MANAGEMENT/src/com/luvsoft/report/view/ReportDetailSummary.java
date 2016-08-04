package com.luvsoft.report.view;

public class ReportDetailSummary {
    private double openingStock; // Tồn đầu kỳ
    private double issueInPeriod; // Xuất trong kỳ
    private double receiptInPeriod; // Nhập trong kỳ
    private double closingStock; // Tồn cuối kỳ

    public ReportDetailSummary() {
        openingStock = 0;
        issueInPeriod = 0;
        receiptInPeriod = 0;
        closingStock = 0;
    }

    public ReportDetailSummary(double openingStock, double issueInPeriod, double receiptInPeriod, double closingStock) {
        this.openingStock = openingStock;
        this.issueInPeriod = issueInPeriod;
        this.receiptInPeriod = receiptInPeriod;
        this.closingStock = closingStock;
    }

    public double getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(double openingStock) {
        this.openingStock = openingStock;
    }

    public double getIssueInPeriod() {
        return issueInPeriod;
    }

    public void setIssueInPeriod(double issueInPeriod) {
        this.issueInPeriod = issueInPeriod;
    }

    public double getReceiptInPeriod() {
        return receiptInPeriod;
    }

    public void setReceiptInPeriod(double receiptInPeriod) {
        this.receiptInPeriod = receiptInPeriod;
    }

    public double getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(double closingStock) {
        this.closingStock = closingStock;
    }

    @Override
    public String toString() {
        return "InOutInventoryRecord [openingStock=" + openingStock + ", issueInPeriod="
                + issueInPeriod + ", receiptInPeriod=" + receiptInPeriod + ", closingStock=" + closingStock + "]";
    }
}