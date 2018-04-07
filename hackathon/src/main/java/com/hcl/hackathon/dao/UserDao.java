package com.hcl.hackathon.dao;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.hcl.hackathon.domain.Login;
import com.hcl.hackathon.domain.UserDetails;
import com.hcl.hackathon.domain.UserRowMapper;

/**
 * Dao class to perform Dao operations
 * @author admin
 *
 */
@Component
public class UserDao {
	
	private final JdbcTemplate jdbcTemplate;  
	  
	@Autowired
	public UserDao( JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;
	}
	
	/**
	 * Method to get login data
	 * @param login
	 * @return
	 */
	public String login(Login login){  
		String query="select u.role from t_user u where u.userId = ? and u.password = ?";
	    List<String> roles = jdbcTemplate.queryForList(query, new Object[] {login.getUserId(), login.getPassword()},String.class); 
	    if (roles.isEmpty()) {
	        return null;
	    } else {
	        return roles.get(0);
	    }
	}

	/**
	 * Method to save user data
	 * @param user
	 * @throws ParseException
	 */
	public void saveUserDetails(UserDetails user) throws ParseException{  
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		java.util.Date dateStr = formatter.parse(user.getDob());
		java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
	    String userDetailsData="insert into t_userdetails values(null,'"+user.getFirstName()+"','"+user.getMiddleName()+"','"+user.getLastName()+"','"+
	     user.getAddressLine1()+"','"+ user.getAddressLine2()+"','"+ user.getCity()+"','" +user.getState()+"','" +user.getPincode()+
	     "','" +user.getContactNo()+"','"+dateDB+"','" +user.getEmailId()+"','" +user.getGender()+"','" +user.getKYCStatus()+"');";
	    jdbcTemplate.update(userDetailsData);  //inserting registration data in user details
	    System.out.println("userdetails===>"+userDetailsData);//TODO to be removed
	    String loginDetailsData="insert into t_user values('"+user.getEmailId()+"','"+user.getPassword()+"','USER');";
	    System.out.println("logindetailsdata===>"+loginDetailsData);//TODO to be removed
	    jdbcTemplate.update(loginDetailsData);  //inserting login data
	}  
	
	/**
	 * Method is used to get all pending kyc user details
	 * @return List<UserDetails>
	 */
	public List<UserDetails> findPendingKycUsers(){  
		String query="select * from t_userdetails ud where ud.KYCStatus = ?";
	    return jdbcTemplate.query(query, new Object[] {"P"}, new UserRowMapper()); 
	}
	
}
