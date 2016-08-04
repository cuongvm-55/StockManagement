package com.luvsoft.report.producer;

import java.util.Date;
import java.util.List;

import com.luvsoft.report.view.AbstractReportDetailsView;

public abstract class AbstractReportDetailsProducer {
    @SuppressWarnings("rawtypes")
    protected AbstractReportDetailsView view;

    protected List<Object> dataList;

    protected Date fromDate;
    protected Date toDate;

    AbstractReportDetailsProducer(){
    }

    /**
     * Generate statistic report in date range [from, to]
     * @param from
     * @param to
     */
    public abstract void generateReport(Date from, Date to, String objectCode);
    public abstract void generateSummaryReportDetails(Date from, Date to, String objectCode);
}
