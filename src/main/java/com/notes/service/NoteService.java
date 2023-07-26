package com.notes.service;

import com.notes.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NoteService {
	void save(String username, Note note);
	void deleteById(int id);
	void deleteAll(String username);
	List<Note> findAllByNoteTextContaining(String userId, String noteText);
	List<Note> findAll(String username);
	Optional<Note> findById(int id);
}
