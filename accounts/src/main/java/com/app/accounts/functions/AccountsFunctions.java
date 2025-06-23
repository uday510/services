package com.app.accounts.functions;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.app.accounts.service.IAccountService;

@Configuration
public class AccountsFunctions {

  private static final Logger logger = LoggerFactory.getLogger(AccountsFunctions.class);

  @Bean
  public Consumer<String> updateCommunication(IAccountService accountService) {
    return accountNumberStr -> {
      Long accountNumber = Long.parseLong(accountNumberStr);
      logger.info("Updating communication status for account: {}", accountNumber);
      accountService.updateCommunicationStatus(accountNumber);
    };
  }

}
