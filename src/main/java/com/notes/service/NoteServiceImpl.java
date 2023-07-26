package com.notes.service;

import com.notes.entity.Note;
import com.notes.entity.NoteUser;
import com.notes.repository.NoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NoteServiceImpl implements NoteService {
	
	private final NoteRepository noteRepository;
	private final NoteUserService noteUserService;
	private final TimeZoneService timeZoneService;
	
	
	@Transactional
	@Override
	public void save(String username, Note note) {
		
		NoteUser noteUser = noteUserService.
			findNoteUserByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(
				"User not found while saving note"));
		
		note.setUser(noteUser);
		note.setNoteText(note.getNoteText().trim());
		note.setDateTime(timeZoneService.clientDateTime());
		
		noteRepository.save(note);
	}
	
	@Transactional
	@Override
	public void deleteById(int id) {
		noteRepository.deleteById(id);
	}
	
	@Transactional
	public void deleteAll(String username) {
		noteRepository.deleteAllByUser_Username(username);
	}
	
	@Override
	public List<Note> findAllByNoteTextContaining(String userId, String noteText) {
		return noteRepository
			.findNoteByUser_UsernameAndNoteTextContainingOrderByDateTimeAsc(userId, noteText);
	}
	
	@Override
	public List<Note> findAll(String username) {
		return noteRepository.findAllByUser_UsernameIsContaining(username);
	}
	
	@Override
	public Optional<Note> findById(int id) {
		return noteRepository.findById(id);
	}
}
