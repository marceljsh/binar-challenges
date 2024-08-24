package com.marceljsh.binarfud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@PropertySource("file:${user.dir}/.env")
public class BinarfudApplication {

  // TODO
  //  [ ] blacklist token because token from app before recompile
  //      is still valid as long as it's not expired
  //  [ ] implement OAuth2
  //  [ ] mail verification
  //  [ ] intercept and inject owner_id when creating request using jwt
  //  [ ] subtract stock after order
  //  [ ] use @PreAuthorize for security
  //  [ ] use improved OrderResponse instead of ReceiptResponse
  //  [ ] split error message into TYPE and MESSAGE
  public static void main(String[] args) {
    SpringApplication.run(BinarfudApplication.class, args);
  }

}
