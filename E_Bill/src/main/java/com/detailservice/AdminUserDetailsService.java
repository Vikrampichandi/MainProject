package com.detailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;

import com.model.Admin;
import com.repository.AdminRepository;

public class AdminUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Admin admin = repo.findByEmail(username);
        if (admin == null) {
            throw new UsernameNotFoundException("No user found with the given email");
        }
        return new AdminUserDetails(admin);
	}

}
