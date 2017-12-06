package com.toptal.demo.security.identity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toptal.demo.entities.User;
import com.toptal.demo.repositories.UserRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

	@Override
    public final TokenUser loadUserByUsername(final String email) throws UsernameNotFoundException, DisabledException {

        final User user = userRepository.findOneByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		TokenUser currentUser;
		currentUser = new TokenUser(user);

		detailsChecker.check(currentUser);
		return currentUser;
	}
}
