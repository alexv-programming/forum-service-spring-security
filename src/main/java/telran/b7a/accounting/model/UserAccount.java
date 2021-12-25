package telran.b7a.accounting.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"login"})
@Builder
@ToString
public class UserAccount {
//	private static final long PASSWORD_EXPIRATION_TIME = 30L * 24L * 60L * 60L * 1000L;//30 days
	@Id
	String login;
//	@Setter
	String password;
	@Setter
	String firstName;
	@Setter
	String lastName;
	@Setter
	LocalDateTime passwordChangeTime;
	@Setter
	@Singular
	List<String> roles = new ArrayList<String>();
	
	


	public UserAccount(String login, String password, String firstName, String lastName) {
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.passwordChangeTime = LocalDateTime.now();
	}
	
	

	public void addRole(String upperCase) {
		roles.add(upperCase);
	}

	public void setPassword(String password) {
		this.password = password;
//		passwordChangeTime = LocalDateTime.now();
	}
	
	public boolean isPasswordExpired() {
		LocalDateTime currentTime = LocalDateTime.now();
		return currentTime.isBefore(passwordChangeTime.plusMonths(1L));
		
	}
	

}
