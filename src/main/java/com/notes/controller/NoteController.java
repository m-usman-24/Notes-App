package com.notes.controller;

import com.notes.entity.Note;
import com.notes.service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/notes")
public class NoteController {

	private final NoteService noteService;
	

	@GetMapping
	public String getNotes(@AuthenticationPrincipal UserDetails user, Model model) {
		List<Note> noteList = noteService.findAll(user.getUsername());
		model.addAttribute("noteList", noteList);

		return "notes";
	}

	@GetMapping("/search")
	public String getNotesBySearch(@AuthenticationPrincipal UserDetails userDetails,
	                               @RequestParam("s") String queryString,
	                               Model model) {
		
		String userid = userDetails.getUsername();

		List<Note> noteList = noteService.findAllByNoteTextContaining(userid, queryString);
		model.addAttribute("noteList", noteList);
		model.addAttribute("queryString", queryString);

		return "notes";
	}

	@PostMapping
	public String save(@ModelAttribute("note") Note note,
	                   HttpServletRequest request,
	                   @AuthenticationPrincipal UserDetails user) {
//		referer is the actual link that is being requested or posted
		String referer = request.getHeader("Referer");
		String redirectLink;

		if (note.getNoteText().trim().length() == 0) {
			if (referer != null && referer.endsWith("/compose")) {
				redirectLink = "redirect:/notes/compose";
			} else if (referer != null && referer.contains("/update")) {
//				adding the referer with prepending redirect: to redirect back to previous page
				redirectLink = "redirect:" + referer;
			} else {
				redirectLink = "redirect:/notes/compose"; // default redirect
			}
		} else {
			noteService.save(user.getUsername(), note);
			redirectLink = "redirect:/notes";
		}

		return redirectLink;
	}

	@GetMapping("/compose")
	public String addNotes(Model model, HttpServletRequest request) {
		Note note = new Note();
		model.addAttribute("note", note);
		model.addAttribute("mapping", request.getRequestURI());

		return "addNote";
	}

	@GetMapping("/update")
	public String updateNote(@RequestParam("id") int id, Model model) {
		Note note = noteService
			.findById(id)
			.orElseThrow(() ->
			new IllegalStateException("Note not Found on the server."));

		model.addAttribute("note", note);

		return "addNote";

	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") int id) {
		noteService.deleteById(id);

		return "redirect:/notes";
	}
	
	@PostMapping("/delete-all")
	public String deleteAll(@AuthenticationPrincipal UserDetails user) {
		noteService.deleteAll(user.getUsername());
		
		return "redirect:/notes";
	}
}
