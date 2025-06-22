package com.app.messagequeue.functions;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.app.messagequeue.dto.AccountMessageDto;
import java.util.function.Function;

@Configuration
public class MessageFunctions {

  private static final Logger logger = LoggerFactory.getLogger(MessageFunctions.class);

  @Bean
  public Function<AccountMessageDto, AccountMessageDto> email() {
    return accountMessageDto -> {
      if (accountMessageDto == null) {
        logger.warn("Received null AccountMessageDto for email.");
        return null;
      }
      logger.info("Sending Email...");
      logger.info("Details ::: {}", accountMessageDto);
      return accountMessageDto;
    };
  }

  @Bean
  public Function<AccountMessageDto, Long> sms() {
    return accountMessageDto -> {
      if (accountMessageDto == null) {
        logger.warn("Received null AccountMessageDto for SMS.");
        return null;
      }
      logger.info("Sending SMS...");
      logger.info("Details: {}", accountMessageDto);
      return accountMessageDto.accountNumber(); 
    };
  }
}
