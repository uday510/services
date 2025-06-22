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
  public Consumer<Long> updateCommunication(IAccountService accountService) {
    return accountNumber -> {
      logger.info("Updating communication status for the account number : {}",
          accountNumber.toString());
      accountService.updateCommunicationStatus(accountNumber);
    };
  }

}
