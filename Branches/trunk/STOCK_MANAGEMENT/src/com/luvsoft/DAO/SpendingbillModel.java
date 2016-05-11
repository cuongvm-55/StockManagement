package com.luvsoft.DAO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Money;
import com.luvsoft.entities.Spendingbill;
import com.luvsoft.entities.Spendingbilldetail;

public class SpendingbillModel extends AbstractEntityModel implements Serializable {
    private static final long serialVersionUID = 1508749070253199168L;

    @Override
    public String getEntityname() {
        return Spendingbill.getEntityname();
    }

    @Override
    public void addNew(AbstractEntity entity) {
        super.addNew(entity);

        // After add new receiving bill, we will update money table
        // Find the last money from database
        Money lastMoney = (Money) entityManager.findLastItem(Money.getEntityname());

        Spendingbill bill = (Spendingbill) entity;
        BigDecimal totalAmount = new BigDecimal(0.0f);
        for (Spendingbilldetail detail : bill.getSpendingbilldetails()) {
            totalAmount = totalAmount.add(detail.getAmount());
        }

        Money money = new Money();
        if(lastMoney == null) {
            money.setAmount(totalAmount);
        } else {
            money.setAmount(lastMoney.getAmount().subtract(totalAmount));
        }
        money.setDate(new Date());
        money.setDescription("Mã phiếu mua hàng " + bill.getCode() + " lý do " + bill.getContent());
        entityManager.addNew(money);
    }

    @Override
    public List<AbstractEntity> getFilterData(FilterObject filterObject) {
        // TODO Auto-generated method stub
        return null;
    }
}
