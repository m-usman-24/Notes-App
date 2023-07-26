package com.notes.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "note_user")
public class NoteUser implements UserDetails {
	
	public NoteUser() {
	}
	
	public NoteUser(String username, String password, String email,
	                String fullName) {
		
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullName = fullName;
	}
	
	@Id
	@Column(name = "username", unique = true)
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "roles")
	@Enumerated(EnumType.STRING)
	private Roles roles;
	
	@Column(name = "enabled")
	private Boolean enabled = false;
	
	@Column(name = "locked")
	private Boolean locked = true;
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(roles.name()));
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
