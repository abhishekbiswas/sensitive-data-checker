package com.barclaycardus.ccd;

import com.barclaycardus.ccd.config.AppConfiguration;
import com.barclaycardus.ccd.manager.ApplicationManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class App {


    public static void main(String[] args) throws IOException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        ApplicationManager applicationManager = context.getBean(ApplicationManager.class);
        applicationManager.process(args);

    }

}
