package com.luvsoft.statistic;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.luvsoft.DAO.EntityManagerDAO;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Material;

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
            // Sanity check
            // if( materialList == null || materialList.isEmpty() ){
            //     return; // ?? need to consume other factors (e.g Customer,...)
            // }
    
            int consumedCount = 0;
            for(int i = 0; i < materialList.size(); i++){
                Material mt = (Material)materialList.get(i);
                if( mt == null ){
                    continue; // next material
                }
                mt.verifyObject();
                if( MaterialStatisticManager.getInstance().isConsumMaterialHistoryNeeded(datePoint, mt.getId()) ){
                    MaterialStatisticManager.getInstance().consumeMaterialHistory(datePoint, mt);
                }
                else{
                    // Already consumed
                    System.out.println("Material history has already consumed!");
                    consumedCount++;
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
                    CustomerStatisticManager.getInstance().consumeCustomerHistory(datePoint, c);
                }
                else{
                    // Already consumed
                    System.out.println("Material history has already consumed!");
                    consumedCount++;
                }
            }

            // All material will be consumed in the same day, and only one day per month
            // If all the record have already consumed
            // We can now sleep the thread to wait (the first day of next month - datePoint)
            try{
                if( consumedCount == ( materialList.size() + customerList.size() ) ){
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(datePoint);
    
                    int waitingDays = cal.getMaximum(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH);
                    waitingDays++; // The first day of the next month
                    System.out.println("All material history are consumed, Thread sleep in "+waitingDays*24*60*60*1000 +" ms");
                    Thread.sleep(waitingDays*24*60*60*1000);
                }
                else{ // wait 5 minutes
                    System.out.println("Thread sleep in "+5*60*1000 +" ms");
                    Thread.sleep(5*60*1000);
                }
            }catch(Exception e){
                System.out.println("StatisticManagerThread::Fail to sleep the thread!!!");
            }
        }
    }
}
