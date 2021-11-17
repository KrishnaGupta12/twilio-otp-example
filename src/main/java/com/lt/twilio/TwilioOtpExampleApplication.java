package com.lt.twilio;

import com.lt.twilio.config.TwilioConfig;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class TwilioOtpExampleApplication {
    @Autowired
    private TwilioConfig twilioConfig;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(twilioConfig.getAccountSId(), twilioConfig.getAuthToken());
    }

    public static void main(String[] args) {
        SpringApplication.run(TwilioOtpExampleApplication.class, args);
    }

}
