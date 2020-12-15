package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.utilities.models.Response;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService.getPrincipal;

@Service
public class NoteService {

    private NoteMapper noteMapper;
    private UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }


    public List <Note> getNotes(){
        User userObject = userMapper.findByUsername(getPrincipal());
        return noteMapper.getNotesByUsername(userObject.getUserId());
    }

    public Note getNote (int id) {
        return noteMapper.getNote(id);
    }

    public Response create (Note note){
        note.setUserid(userMapper.findByUsername(getPrincipal()).getUserId());

        if (noteMapper.findByNoteTitle(note.getNotetitle()).isPresent() && note.getNoteid() == null) {
            return new Response("999", false, true,"Note Title already exists, Use another!", "");
        }


        if (note.getNoteid() == null) {
            Integer noteid = noteMapper.insert(note);
            return new Response("000", true , false,"Note created successfully", noteid);

        } else {
            noteMapper.edit(note.getNoteid(), note);
            return new Response("000", true, false,"Note updated successful", "");
        }

    }


    public Response delete(int id){
        if(noteMapper.findByNoteId(id).isPresent()) {
            noteMapper.delete(id);
            return new Response("000", true, false, "Note deleted successfully","");
        }
        return new Response("999", false, true,"Note deletion failed", "");
    }


}
