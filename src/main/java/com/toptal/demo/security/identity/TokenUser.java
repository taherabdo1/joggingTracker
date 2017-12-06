package com.toptal.demo.security.identity;


import org.springframework.security.core.authority.AuthorityUtils;

import com.toptal.demo.entities.User;


public class TokenUser extends org.springframework.security.core.userdetails.User {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private User user;

	// For returning a normal user
	public TokenUser(final User user) {
        super(user.getEmail(), user.getPassword(), user.getActivated(), true, true, !user.isBlocked(),
		 AuthorityUtils.createAuthorityList(user.getRole().toString()));
		this.user = user;
		
	}

	public User getUser() {
		return user;
	}

	public String getRole() {
		return user.getRole().toString();
	}
}
