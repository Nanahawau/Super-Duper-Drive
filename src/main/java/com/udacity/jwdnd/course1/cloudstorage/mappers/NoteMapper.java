package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getNotesByUsername(int userId);

    @Select("SELECT * FROM NOTES WHERE notetitle = #{notetitle}")
    Optional<Note> findByNoteTitle (String notetitle);

    @Select("SELECT * FROM NOTES WHERE noteid = #{id}")
    Optional<Note> findByNoteId (int id);


    @Select("SELECT * FROM NOTES WHERE noteid = #{id}")
    Note getNote (int id);

    @Insert("INSERT INTO NOTES(notetitle, notedescription, userid) " +
            "VALUES(#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insert(Note note);

    @Update("update NOTES set notetitle =#{note.notetitle}, notedescription=#{note.notedescription} where noteid=#{id}")
    void edit (@Param("id") int id, @Param("note") Note note);


    @Delete("DELETE FROM NOTES WHERE noteid = #{id}")
    void delete(Integer id);
}
