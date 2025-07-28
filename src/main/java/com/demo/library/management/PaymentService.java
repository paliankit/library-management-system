//package com.demo.library.management;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PaymentService {
//    @Autowired
//    private NotificationService notificationService;
//
//
//    public void makePayment(){
//        System.out.println("Processing Payment");
//        //after payment is done, send notification
//        notificationService.sendNotification("Payment Processed");
//    }
//
//    public String getTransactionDetails(){
//        return "These are the other transaction related details";
//    }
//}
