package com.notes.repository;

import com.notes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
	
	List<Note> findNoteByUser_UsernameAndNoteTextContainingOrderByDateTimeAsc(String username, String noteText);
	List<Note> findAllByUser_UsernameIsContaining(String username);
	@Modifying
	@Query("update Note n set n.noteText = :noteText " +
		   "where n.id = :id and n.user.username = :username")
	void updateNoteById(@Param("noteText") String noteText,
	                    @Param("id") int id,
	                    @Param("username") String username);
	@Modifying
	void deleteAllByUser_Username(String username);
}
