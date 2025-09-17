package com.codeology.dto;

import lombok.*;

import java.util.Set;


@Data
@Builder
public class JwtResponse {
	
    
	private String token;
	
	@Builder.Default
    private String tokenType = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    
	public JwtResponse(String token, String tokenType, Long id, String username, String email, Set<String> roles) {
		super();
		this.token = token;
		this.tokenType = tokenType;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}
    
    
    
}
