package com.app.messagequeue.functions;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.app.messagequeue.dto.AccountMessageDto;
import java.util.function.Function;

@Configuration
public class MessageFunctions {

  private static final Logger Logger = LoggerFactory.getLogger(MessageFunctions.class);

  @Bean
  public Function<AccountMessageDto, AccountMessageDto> email() {
    return accountMessageDto -> {
      Logger.info("Sending Email ..." + accountMessageDto.toString());
      Logger.info("Details ::: " + accountMessageDto.toString());
      return accountMessageDto;
    };
  }

  @Bean
  public Function<AccountMessageDto, Long> sms() {
    return accountMessageDto -> {
      Logger.info("Sending SMS ... " + accountMessageDto.toString());
      Logger.info("Details ::: " + accountMessageDto.toString());
      return accountMessageDto.accountNumber();
    };
  }


}

