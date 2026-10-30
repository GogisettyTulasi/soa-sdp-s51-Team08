package com.klef.sdp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.klef.sdp.entity.Role;
import com.klef.sdp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	//from user where email=?1 and password=?2
		Optional<User> findByEmailAndPassword(String email,String password);
		
		Optional<User> findByEmail(String email); 
		
		List<User> findByName(String name); 
		
		List<User> findByRole(Role role); 
}