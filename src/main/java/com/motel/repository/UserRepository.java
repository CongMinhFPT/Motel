package com.motel.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.motel.entity.Status;
import com.motel.entity.User;

import java.util.List;

public interface UserRepository  extends MongoRepository<User, String> {
    List<User> findAllByStatus(Status status);
    
    @Query("{'nickName': { $ne: ?0 }}")
    List<User> findAllWithMessages(String nickname);
    @Query("{'nickName': { $in: ?0 }}")
    List<User> findAllByNickNameIn(List<String> nicknames);
}
