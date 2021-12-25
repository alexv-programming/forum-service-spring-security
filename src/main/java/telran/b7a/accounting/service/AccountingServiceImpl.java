package telran.b7a.accounting.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import telran.b7a.accounting.dao.AccountingMongoRepository;
import telran.b7a.accounting.dto.RegisterUserDto;
import telran.b7a.accounting.dto.ResponseRoleDto;
import telran.b7a.accounting.dto.ResponseUserDto;
import telran.b7a.accounting.dto.UpdateUserDto;
import telran.b7a.accounting.dto.exception.UserExistsException;
import telran.b7a.accounting.dto.exception.UserNotFoundException;
import telran.b7a.accounting.model.UserAccount;

@Service
public class AccountingServiceImpl implements AccountingService {

	AccountingMongoRepository repository;
	ModelMapper modelMapper;
	PasswordEncoder passwordEncoder;

	@Autowired
	public AccountingServiceImpl(AccountingMongoRepository repository, ModelMapper modelMapper,
			PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
//	public ResponseUserDto registerUser(RegisterUserDto registerUserDto) {
//		UserAccount user = modelMapper.map(registerUserDto, UserAccount.class);
//		repository.save(user);
//		return modelMapper.map(user, ResponseUserDto.class);							
//	}
//	
	public ResponseUserDto registerUser(RegisterUserDto registerUserDto) {
		if (repository.existsById(registerUserDto.getLogin())) {
			throw new UserExistsException(registerUserDto.getLogin());
		}
		UserAccount userAccount = modelMapper.map(registerUserDto, UserAccount.class);
		userAccount.addRole("USER".toUpperCase());
		String password = passwordEncoder.encode(registerUserDto.getPassword());
		userAccount.setPassword(password);
		repository.save(userAccount);
		return modelMapper.map(userAccount, ResponseUserDto.class);
	}

	@Override
	public ResponseUserDto getUser(String login) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		return modelMapper.map(userAccount, ResponseUserDto.class);
	}

	@Override
	public ResponseUserDto deleteUser(String user) {
		UserAccount userInst = repository.findById(user).orElseThrow(() -> new UserNotFoundException(user));
		repository.deleteById(user);
		return modelMapper.map(userInst, ResponseUserDto.class);
	}

	@Override
	public ResponseUserDto updateUser(String user, UpdateUserDto updateUserDto) {
		UserAccount userInst = repository.findById(user).orElseThrow(() -> new UserNotFoundException(user));
		String firstName = updateUserDto.getFirstName();
		if (firstName != null) {
			userInst.setFirstName(firstName);
		}
		String lastName = updateUserDto.getLastName();
		if (lastName != null) {
			userInst.setLastName(lastName);
		}
		repository.save(userInst);
		return modelMapper.map(userInst, ResponseUserDto.class);
	}

	@Override
	public ResponseRoleDto addRole(String user, String role) {
		UserAccount userInst = repository.findById(user).orElseThrow(() -> new UserNotFoundException());
		userInst.getRoles().add(role.toUpperCase());
		repository.save(userInst);
		return modelMapper.map(userInst, ResponseRoleDto.class);
	}

	@Override
	public ResponseRoleDto deleteRole(String user, String role) {
		UserAccount userInst = repository.findById(user).orElseThrow(() -> new UserNotFoundException());
		userInst.getRoles().remove(role.toUpperCase());
		
		repository.save(userInst);
		return modelMapper.map(userInst, ResponseRoleDto.class);
	}

	@Override
	public void changePassword(String user, String password) {
		UserAccount userAccount = repository.findById(user).orElseThrow(() -> new UserNotFoundException());
//		if (userAccount.getLastChangePasswordDate().plus(amountToAdd)) {
//			
//		}
		userAccount.setPassword(passwordEncoder.encode(password));
		repository.save(userAccount);
	}

}
