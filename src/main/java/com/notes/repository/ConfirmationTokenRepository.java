package com.notes.repository;

import com.notes.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
	Optional<ConfirmationToken> findByToken(String token);
	@Modifying
	void deleteByNoteUser_Username(String username);
	@Modifying
	void deleteByNoteUser_Email(String email);
	@Modifying
	@Query("update ConfirmationToken c set c.confirmedAt = :confirmedAt where c.token = :token")
	void updateConfirmedAt(
		@Param("token") String token,
		@Param("confirmedAt") LocalDateTime confirmedAt);
	
	@Query("select c.noteUser.username from ConfirmationToken c where c.token = ?1")
	Optional<String> findNoteUserByToken(String token);
}
