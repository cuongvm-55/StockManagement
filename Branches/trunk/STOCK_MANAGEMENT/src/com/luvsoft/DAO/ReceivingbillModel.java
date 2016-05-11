package com.luvsoft.DAO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Money;
import com.luvsoft.entities.Receivingbill;
import com.luvsoft.entities.Receivingbilldetail;

public class ReceivingbillModel extends AbstractEntityModel implements Serializable {
    private static final long serialVersionUID = 3700106855651539733L;

    @Override
    public String getEntityname() {
        return Receivingbill.getEntityname();
    }

    @Override
    public void addNew(AbstractEntity entity) {
        super.addNew(entity);

        // After add new receiving bill, we will update money table
        // Find the last money from database
        Money lastMoney = (Money) entityManager.findLastItem(Money.getEntityname());

        Receivingbill bill = (Receivingbill) entity;
        BigDecimal totalAmount = new BigDecimal(0.0f);
        for (Receivingbilldetail detail : bill.getReceivingbilldetails()) {
            totalAmount = totalAmount.add(detail.getAmount());
        }

        Money money = new Money();
        if(lastMoney == null) {
            money.setAmount(totalAmount);
        } else {
            money.setAmount(lastMoney.getAmount().add(totalAmount));
        }
        money.setDate(new Date());
        money.setDescription("Mã phiếu thu " + bill.getCode() + " lý do " + bill.getContent());
        entityManager.addNew(money);
    }

    @Override
    public List<AbstractEntity> getFilterData(FilterObject filterObject) {
        // TODO Auto-generated method stub
        return null;
    }
}
