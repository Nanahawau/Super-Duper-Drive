package com.udacity.jwdnd.course1.cloudstorage.mappers;
import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getFileByUsername (int userId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    Optional<File> findFileByFilename (String filename);

    @Select("SELECT * FROM FILES WHERE filename = #{name}")
    Optional<File> findByFileName (String name);

    @Select("SELECT * FROM FILES WHERE fileId = #{id}")
    File getFile (int id);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userId, filedata) " +
            "VALUES(#{filename}, #{contenttype}, #{filesize}, #{userId}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Update("update FILES set filename=#{file.filename}, contenttype=#{file.contenttype}, filesize=#{file.filesize}, " +
            "userId=#{file.userId}, filedata=#{file.filedata}  where fileId=#{id}")
    void edit (@Param("id") int id, @Param("file") File file);


    @Delete("DELETE FROM FILES WHERE filename = #{name}")
    void delete(String name);
}
