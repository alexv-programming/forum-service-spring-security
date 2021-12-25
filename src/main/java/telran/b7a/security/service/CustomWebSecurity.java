package telran.b7a.security.service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import telran.b7a.accounting.dao.AccountingMongoRepository;
import telran.b7a.accounting.dto.exception.UserNotFoundException;
import telran.b7a.accounting.model.UserAccount;
import telran.b7a.forum.dao.ForumMongoRepository;
import telran.b7a.forum.model.Post;

@Service("customSecurity")
public class CustomWebSecurity {
	
	ForumMongoRepository fRepository;
	AccountingMongoRepository aRepository;
	
	
	@Autowired
	public CustomWebSecurity(ForumMongoRepository fRepository, AccountingMongoRepository aRepository) {
		this.fRepository = fRepository;
		this.aRepository = aRepository;
	}

	public boolean checkPostAuthority(String postId, String userName) {
		Post post = fRepository.findById(postId).orElse(null);
		return post == null || userName.equals(post.getAuthor());
	}
	
	public boolean checkIfPasswordExpired(String userName, HttpServletRequest request) {		
		String method = request.getMethod();
		String path = request.getServletPath();
		if ("PUT".equalsIgnoreCase(method) && path.matches("/account/password")) {
			return true;
		}
		UserAccount userAccount = aRepository.findById(userName).orElse(null);
		if(userAccount == null) {
			return true;
		}
		return userAccount
				.isPasswordExpired();
				
	}

}


