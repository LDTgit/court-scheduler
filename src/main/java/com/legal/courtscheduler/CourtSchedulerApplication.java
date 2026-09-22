package com.legal.courtscheduler;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourtSchedulerApplication {
    public static void main(String[] args){
        Application.launch(JavaFxApplication.class, args);
    }
}
