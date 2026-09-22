package com.legal.courtscheduler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class JavaFxApplication extends Application {
    private static ConfigurableApplicationContext applicationContext;

    @Override
    public void init(){
        applicationContext= SpringApplication.run(CourtSchedulerApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);

        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Programari instanta");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void stop(){
        applicationContext.close();
        Platform.exit();
    }

    public static ConfigurableApplicationContext getApplicationContext(){
        return applicationContext;
    }
}
