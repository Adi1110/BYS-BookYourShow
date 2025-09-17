package com.codeology.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
	
    public JwtResponse(String token2, String string, Long id2, String username2, String email2, Set<String> roles2) {
		// TODO Auto-generated constructor stub
	}
	private String token;
    private String tokenType = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
}
