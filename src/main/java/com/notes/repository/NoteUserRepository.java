package com.notes.repository;

import com.notes.entity.NoteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteUserRepository extends JpaRepository<NoteUser, String> {
	
	boolean existsByEmailIgnoreCase(String email);
	Optional<NoteUser> findNoteUserByUsername(String username);
	Optional<NoteUser> findNoteUserByEmail(String email);
	@Query("select n from NoteUser n where n.username = :username and n.enabled")
	Optional<NoteUser> findNoteUserWhereEnabled(@Param("username") String userName);
	@Query("select n.password from NoteUser n where n.username = ?1")
	Optional<String> findPasswordByUsername(String username);
}
