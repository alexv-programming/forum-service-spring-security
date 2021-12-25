package telran.b7a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import telran.b7a.accounting.dao.AccountingMongoRepository;
import telran.b7a.accounting.model.UserAccount;

@SpringBootApplication
public class ForumWebServiceSpringSecurityApplication implements CommandLineRunner {
	
	AccountingMongoRepository repository;
	PasswordEncoder passwordEncoder;
	
	@Autowired
	public ForumWebServiceSpringSecurityApplication(AccountingMongoRepository repository,
			PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(ForumWebServiceSpringSecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(!repository.existsById("admin")) {
			String password = passwordEncoder.encode("admin");
			UserAccount userAccount = new UserAccount("admin", password, "", "");
			userAccount.addRole("USER".toUpperCase());
			userAccount.addRole("Moderator".toUpperCase());
			userAccount.addRole("administrator".toUpperCase());;
			repository.save(userAccount);
		}
	}
}
