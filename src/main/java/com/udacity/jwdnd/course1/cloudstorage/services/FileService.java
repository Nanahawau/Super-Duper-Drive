package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.utilities.models.Response;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService.getPrincipal;

@Service
public class FileService {

    private FileMapper fileMapper;
    private UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public List<File> getFiles (){
        User userObject = userMapper.findByUsername(getPrincipal());
        return fileMapper.getFileByUsername(userObject.getUserId());
    }

    public File getFile (int id) {
        return fileMapper.getFile(id);
    }

    public Response create (MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (fileName.equals("")){
            return new Response("999", false, true,"No file uploaded, try again!", "");
        }
        File newFile = new File(fileName, file.getContentType(), String.valueOf(file.getSize()), userMapper.findByUsername(getPrincipal()).getUserId(), file.getBytes());

        if (fileMapper.findFileByFilename(newFile.getFilename()).isPresent() && newFile.getFileId() == null) {
            return new Response("999", false, true,"Filename already exists", "");
        } else {
            Integer noteid = fileMapper.insert(newFile);
            return new Response("000", true , false, "File created successfully", noteid);

        }

    }


    public Response delete(String name){
        if(fileMapper.findByFileName(name).isPresent()) {
            fileMapper.delete(name);
            return new Response("000", true, false,"File deleted successful", "");
        }
        return new Response("999", false, true, "File delete failed","");
    }

}
