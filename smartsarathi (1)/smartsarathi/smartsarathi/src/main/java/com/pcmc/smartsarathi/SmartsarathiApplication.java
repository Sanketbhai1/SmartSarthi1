package com.pcmc.smartsarathi;

import com.pcmc.smartsarathi.utils.LogDirectoryCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartsarathiApplication {

	public static void main(String[] args) {
		LogDirectoryCreator.createLogFolder();
		SpringApplication.run(SmartsarathiApplication.class, args);
		System.err.println("Welcome to SMART SARATHI APPLICATION");
	}

}
