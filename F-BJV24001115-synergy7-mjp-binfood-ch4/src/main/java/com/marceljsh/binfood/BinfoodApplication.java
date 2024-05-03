package com.marceljsh.binfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BinfoodApplication {

	// TODO: replace all exceptions with ResponseStatusException
	// TODO: use builder instead of constructor for model
	// TODO: implement Serializable on every model for JSON
	// TODO: REST API using JWT
	// TODO: Auditable (id, created, updated, deleted) -> nullable
	//			- use @Temporal(TemporalType.TIMESTAMP)
	//			- use custom constructor instead of lombok
	//			- use @CreatedDate and @LastModifiedDate
	//			- (optional) use @CreatedBy and @LastModifiedBy
	//			- (optional) use @PrePersist and @PreUpdate
	//			- (optional) use @EntityListeners(AuditingEntityListener.class)
	// TODO: tackle api abuse (rate limiting, etc.)
	// TODO: authorization (role-based, permission-based)
	// TODO: use Stored Procedures for complex queries and insertions
	// TODO: use built-in hash

	public static void main(String[] args) {
		SpringApplication.run(BinfoodApplication.class, args);
	}

}
