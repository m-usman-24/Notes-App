package com.notes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {
	
	@Id
	@SequenceGenerator(
		name = "token_generator",
		sequenceName = "token_generator",
		allocationSize = 1
	)
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "token_generator"
	)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "token", nullable = false)
	private String token;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "expires_at", nullable = false)
	private LocalDateTime expiresAt;
	
	@Column(name = "confirmed_at")
	private LocalDateTime confirmedAt;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "note_user_username", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	@ToString.Exclude
	private NoteUser noteUser;
	
	public ConfirmationToken(String token, LocalDateTime createdAt,
	                         LocalDateTime expiresAt, NoteUser noteUser) {
		this.token = token;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
		this.noteUser = noteUser;
	}
}
