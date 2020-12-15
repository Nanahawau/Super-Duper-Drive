package com.udacity.jwdnd.course1.cloudstorage.controllers;
import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.utilities.models.Response;
import com.udacity.jwdnd.course1.cloudstorage.utilities.models.ResponseFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Controller
public class HomeController {

    private NoteService noteService;
    private CredentialService credentialService;
    private FileService fileService;

    public HomeController(NoteService noteService, CredentialService credentialService, FileService fileService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping("/home")
    public String getNotes (Note note, File file, Credential credential, Model model)
    {
        List<Note> notes = noteService.getNotes();
        List<Credential> credentials = credentialService.getCredentials();
        List<ResponseFile> files = fileService.getFiles().stream().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(String.valueOf(dbFile.getFileId()))
                    .toUriString();
            return new ResponseFile(
                    dbFile.getFilename(),
                    fileDownloadUri,
                    dbFile.getContenttype(),
                    dbFile.getFiledata().length);
        }).collect(Collectors.toList());

        model.addAttribute("notes", notes);
        model.addAttribute("credentials", credentials);
        model.addAttribute("files", files);

        return "home";
    }

    @GetMapping("/result")
    public String getResultPage (@RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "success", required = false) String success){

        return "result";
    }

    @PostMapping("/createNote")
    public String createNote (@Valid Note note, BindingResult bindingResult, Model model, HttpSession session) {
        boolean success;
        boolean error;

        if(bindingResult.hasErrors()){
            model.addAttribute("noteForm", note);
            return "signup";
        }
        try {
            Response response =  noteService.create(note);
            session.setAttribute("message", response.getResponseMessage());
             success = response.isResponseSuccess();
             error = response.isResponseFailed();

        }catch (Exception e){
            bindingResult.rejectValue("notetitle", "note.notetitle","Note title cannot be empty");
            bindingResult.rejectValue("notedescription", "note.notedescription","Note Description cannot be empty");
            model.addAttribute("noteForm", note);
            return "home";
        }

        return MessageFormat.format("redirect:/result?error={1}&success={0}", success, error );
    }



    @GetMapping("/deleteNote")
    public String deleteNote(@RequestParam("id") int id, HttpSession session){
        Response response = noteService.delete(id);
        session.setAttribute("message", response.getResponseMessage());
        boolean success = response.isResponseSuccess();
        boolean error = response.isResponseFailed();
        return MessageFormat.format("redirect:/result?error={1}&success={0}", success, error );
    }


    @PostMapping("/createCredential")
    public String createCredential (@Valid Credential credential,  BindingResult bindingResult, Model model, HttpSession session) {

        boolean success;
        boolean error;

        if(bindingResult.hasErrors()){
            model.addAttribute("credForm", credential);
            return "signup";
        }
        try {
            Response response =  credentialService.create(credential);
            session.setAttribute("message", response.getResponseMessage());
            success = response.isResponseSuccess();
            error = response.isResponseFailed();

        }catch (Exception e){
            bindingResult.rejectValue("", "credential.url","Url is empty");
            bindingResult.rejectValue("", "credential.username","Username is empty");
            bindingResult.rejectValue("", "credential.password","Password is empty");
            model.addAttribute("credForm", credential);
            return "home";
        }

        return MessageFormat.format("redirect:/result?error={1}&success={0}", success, error );
    }



    @GetMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("id") int id, HttpSession session){
        Response response = credentialService.delete(id);
        session.setAttribute("message", response.getResponseMessage());
        boolean success = response.isResponseSuccess();
        boolean error = response.isResponseFailed();
        return MessageFormat.format("redirect:/result?error={1}&success={0}", success, error );
    }

    @GetMapping("/decryptedPassword/{credentialId}")
    @ResponseBody
    public List<String> decryptCredential(@PathVariable("credentialId") Integer credentialId, HttpSession session,
                                          ModelMap model){
        return Arrays.asList(credentialService.decryptedPassword(credentialId));
    }



    @PostMapping("/uploadFile")
    public String uploadFile (@RequestParam("fileUpload") MultipartFile fileUpload, HttpSession session) throws IOException {
      Response response = fileService.create(fileUpload);
        session.setAttribute("message", response.getResponseMessage());
        boolean success = response.isResponseSuccess();
        boolean error = response.isResponseFailed();
        return MessageFormat.format("redirect:/result?error={1}&success={0}", success, error );
    }

    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam("name") String name, HttpSession session){
        Response response = fileService.delete(name);
        session.setAttribute("message", response.getResponseMessage());
        boolean success = response.isResponseSuccess();
        boolean error = response.isResponseFailed();
        return MessageFormat.format("redirect:/result?error={1}&success={0}", success, error );
    }


    @GetMapping("/files/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable int fileId) {
        // Load file from database
        File file = fileService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

}
