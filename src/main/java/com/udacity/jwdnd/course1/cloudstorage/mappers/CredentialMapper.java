package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    List<Credential> getCredentialByUsername(int userId);

    @Select("SELECT * FROM CREDENTIALS WHERE username = #{username}")
    Optional<Credential> findByCredentialUsername (String username);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{id}")
    Optional<Credential> findByCredentialId (int id);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{id}")
    Credential getCredential (int id);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userId) " +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Update("update CREDENTIALS set url=#{credential.url}, username=#{credential.username}, key=#{credential.key}, " +
            "password=#{credential.password}, userId=#{credential.userId}  where credentialId=#{id}")
    void edit (@Param("id") int id, @Param("credential") Credential credential);


    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{id}")
    void delete(Integer id);

}
