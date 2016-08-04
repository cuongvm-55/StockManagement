package com.luvsoft.statistic;

import java.util.Date;
import java.util.List;

import com.luvsoft.DAO.EntityManagerDAO;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Material;
import com.luvsoft.utils.Utilities;

public class StatisticManagerThread extends Thread{
    private static StatisticManagerThread instance = null;
    
    public static StatisticManagerThread getInstance(){
        if( instance == null ){
            instance = new StatisticManagerThread();
        }

        return instance;
    }

    @Override
    public void run() {
        super.run();
        
        while(true){
            Date datePoint = new Date(); // current date time
    
            System.out.println("StatisticManagerThread running...");
            /////////////////////////////////////////////////////////////////////////////////////////////////////
            // Materialhistory
            ////////////////////////////////////////////////////////////////////////////////////////////////////
    
            // Check if we need the consuming
            List<Object> materialList = EntityManagerDAO.getInstance().findAll(Material.getEntityname());
            for(int i = 0; i < materialList.size(); i++){
                Material mt = (Material)materialList.get(i);
                if( mt == null ){
                    continue; // next material
                }
                mt.verifyObject();
                if( MaterialStatisticManager.getInstance().isConsumMaterialHistoryNeeded(datePoint, mt.getId()) ){
                    MaterialStatisticManager.getInstance().consumeMaterialHistory(Utilities.reachMonthBegin(datePoint), mt);
                }
                else{
                    // Already consumed
                    System.out.println("Material history has already consumed!");
                }
            }

            /////////////////////////////////////////////////////////////////////////////////////////////////////
            // Customerhistory
            ////////////////////////////////////////////////////////////////////////////////////////////////////
            List<Object> customerList = EntityManagerDAO.getInstance().findAll(Customer.getEntityname());
            for(int i = 0; i < customerList.size(); i++){
                Customer c = (Customer)customerList.get(i);
                if( c == null ){
                    continue; // next customer
                }
                c.verifyObject();
                if( CustomerStatisticManager.getInstance().isConsumeCustomerHistoryNeeded(datePoint, c.getId()) ){
                    CustomerStatisticManager.getInstance().consumeCustomerHistory(Utilities.reachMonthBegin(datePoint), c);
                }
                else{
                    // Already consumed
                    System.out.println("Material history has already consumed!");
                }
            }

            System.out.println("Consume finished, sleep Thread 1 day");
            try {
                Thread.sleep(24*60*1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
