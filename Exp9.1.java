// File: DemoApp.java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

// Service class
class MyService {
    public void serve() {
        System.out.println("Service is running...");
    }
}

// Controller class that depends on MyService
class MyController {
    private MyService myService;

    // Constructor-based Dependency Injection
    public MyController(MyService myService) {
        this.myService = myService;
    }

    public void process() {
        System.out.println("Controller is processing...");
        myService.serve();
    }
}

// Java-based Spring configuration
@Configuration
class AppConfig {

    @Bean
    public MyService myService() {
        return new MyService();
    }

    @Bean
    public MyController myController() {
        return new MyController(myService());
    }
}

// Main application
public class DemoApp {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MyController controller = context.getBean(MyController.class);
        controller.process();
    }
}

