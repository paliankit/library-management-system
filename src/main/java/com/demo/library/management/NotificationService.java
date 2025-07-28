//package com.demo.library.management;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class NotificationService {
//
//    @Autowired
//    private PaymentService paymentService;
//
//
//    public void sendNotification(String message){
//        System.out.println("Message recived from payment service: "+message);
//        //after reciving message, we need more transaction details from payment service
//        paymentService.getTransactionDetails();
//    }
//}
