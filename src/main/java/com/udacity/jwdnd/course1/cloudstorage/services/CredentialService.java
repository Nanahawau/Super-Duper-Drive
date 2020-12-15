package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.utilities.models.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import static com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService.getPrincipal;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private UserMapper userMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials (){
        User userObject = userMapper.findByUsername(getPrincipal());
        return credentialMapper.getCredentialByUsername(userObject.getUserId());
    }

    public Credential getCredential (int id) {
        return credentialMapper.getCredential(id);
    }
    public Response create (Credential credential){
       String key = getEncryptionKey();
       String password = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setUserId(userMapper.findByUsername(getPrincipal()).getUserId());
        credential.setKey(key);
        credential.setPassword(password);

        if (credentialMapper.findByCredentialUsername(credential.getUsername()).isPresent() && credential.getCredentialId() == null) {
            return new Response("999", false, true,"Credential with this username already exists", "");
        }


        if (credential.getCredentialId() == null) {
            Integer credentialId = credentialMapper.insert(credential);
            return new Response("000", true , false, "Credential creation was successful", credentialId);

        } else {
            credentialMapper.edit(credential.getCredentialId(), credential);
            return new Response("000", true, false, "Credential Creation Failed", "");
        }

    }

//    public Response edit (int id, Note note) {
//        if(noteMapper.findByNoteId(id).isPresent()){
//            noteMapper.edit(id, note);
//            return new Response("000", "Note Update Successful", "");
//        }
//        return new Response("999", "Note Update Failed", "");
//    }

    public Response delete(int id){
        if(credentialMapper.findByCredentialId(id).isPresent()) {
            credentialMapper.delete(id);
            return new Response("000", true, false,"Delete Operation successful", "");
        }
        return new Response("999", false, true,"Delete Operation Failed", "");
    }

    public String getEncryptionKey () {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        return encodedKey;
    }

    public String decryptedPassword (int credentialId){
        Credential credential = credentialMapper.getCredential(credentialId);
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }

}
