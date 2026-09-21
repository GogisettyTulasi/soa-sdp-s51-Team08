package com.klef.sdp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients 
public class BookingServiceApplication 
{
	public static void main(String[] args) {
		SpringApplication.run(BookingServiceApplication.class, args);
		System.out.println("Booking service is running...");
	}
}
