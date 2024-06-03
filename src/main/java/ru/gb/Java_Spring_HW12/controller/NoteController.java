package ru.gb.Java_Spring_HW12.controller;

import ru.gb.Java_Spring_HW12.model.Note;
import ru.gb.Java_Spring_HW12.service.FileGateAway;
import ru.gb.Java_Spring_HW12.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    private final FileGateAway fileGateAway;
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        return new ResponseEntity<>(noteService.getAllNotes(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        fileGateAway.writeToFile(note.getTitle() + ".txt", note.toString());
        return new ResponseEntity<>(noteService.addNote(note), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") Long id) {
        Note noteById;
        try {
            noteById = noteService.getNoteById(id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Note());
        }
        return new ResponseEntity<>(noteById, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public Note updateNoteStatus(@PathVariable Long id, @RequestBody Note.Status status) {
        return noteService.updateNoteStatus(id, status);
    }
    @GetMapping("/status/{status}")
    public List<Note> getNoteByStatus(@PathVariable Note.Status status) {
        return noteService.getNoteByStatus(status);
    }
    @PutMapping
    public ResponseEntity<Note> updateNote(@RequestBody Note note) {
        return new ResponseEntity<>(noteService.updateNote(note), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") Long id) {
        noteService.deleteNoteById(id);
        return ResponseEntity.ok().build();
    }
}