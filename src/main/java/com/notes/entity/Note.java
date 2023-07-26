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
@ToString
@NoArgsConstructor
@Entity
@Table(name = "Notes")
public class Note {
	
	public Note(String note, LocalDateTime dateTime) {
		this.noteText = note;
		this.dateTime = dateTime;
	}
	
	@Id
	@Column(name = "id", columnDefinition = "INT")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	int id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "note_user_username", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ToString.Exclude
	@JsonIgnore
	private NoteUser user;
	
	@Column(name = "time", columnDefinition = "DATETIME")
	private LocalDateTime dateTime;
	
	@Column(name = "note", columnDefinition = "TEXT")
	private String noteText;
	
}
