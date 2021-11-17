package com.lt.twilio.service;

import com.lt.twilio.config.TwilioConfig;
import com.lt.twilio.dto.PasswordResetRequestDto;
import com.lt.twilio.dto.PasswordResetResponseDto;
import com.lt.twilio.dto.otpStatus;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TwilioOTPService {

    @Autowired
    private TwilioConfig twilioConfig;

    Map<String, String> otpMap = new HashMap<>();

    public Mono<PasswordResetResponseDto> sendOTPForPasswordReset(PasswordResetRequestDto passwordResetRequestDto) {
        PasswordResetResponseDto passwordResetResponseDto = null;
        try {
            PhoneNumber to = new PhoneNumber(passwordResetRequestDto.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
            String otp = sendOTP();
            String otpMessage = " Dear Customer , your otp is ##" + otp + "##. use this passcode for complete your transaction. Thank You.";
            Message message = Message
                    .creator(to, from, otpMessage)
                    .create();
            otpMap.put(passwordResetRequestDto.getUserName(), otp);
            passwordResetResponseDto = new PasswordResetResponseDto(otpStatus.DELIVERED, otpMessage);
        } catch (Exception ex) {
            passwordResetResponseDto = new PasswordResetResponseDto(otpStatus.FAILED, ex.getMessage());
        }
        return Mono.just(passwordResetResponseDto);


    }

    private String sendOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }

    public Mono<String> validateOTP(String userInputOtp, String userName) {
        if (userInputOtp.equals(otpMap.get(userName))) {
            return Mono.just("Valid Otp please proceed with your transaction : ");
        } else {
            return Mono.error(new IllegalArgumentException("Invalid Otp Please retry "));
        }

    }

}