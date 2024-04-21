package com.motel.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.motel.entity.ChatMessage;
import com.motel.entity.Status;
import com.motel.entity.User;
import com.motel.repository.ChatMessageRepository;
import com.motel.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final ChatMessageRepository chatMessageRepository;

    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        repository.save(user);
    }

    public void disconnect(User user) {
        var storedUser = repository.findById(user.getNickName()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }
    }

    public List<User> findConnectedUsers() {
        return repository.findAllByStatus(Status.ONLINE);
    }
    
    public List<User> allUser(String nickname) {
        return repository.findAllWithMessages(nickname);
    }
    
//    public List<User> findAllUsersWithMessages(String nickname) {
//        List<String> nicknames = repository.findAllByStatus(Status.ONLINE).stream()
//                .map(User::getNickName)
//                .collect(Collectors.toList());
//        nicknames.remove(nickname); 
//        return repository.findAllByNickNameIn(nicknames);
//    }
    
    public List<User> findAllUsersWithMessages(String nickname) {
        List<String> allUserNicknames = repository.findAll().stream()
                .map(User::getNickName)
                .collect(Collectors.toList());
        allUserNicknames.remove(nickname);
        return repository.findAllByNickNameIn(allUserNicknames);
    }
    
    public List<User> findUsersWithMessages(String nickname) {
        List<String> relatedUserIds = chatMessageRepository.findAll().stream()
                .filter(chatMessage -> !(chatMessage.getSenderId().equals(nickname) && chatMessage.getRecipientId().equals(nickname)))
                .map(chatMessage -> chatMessage.getSenderId().equals(nickname) ? chatMessage.getRecipientId() : chatMessage.getSenderId())
                .distinct()
                .collect(Collectors.toList());
        System.out.println("relatedUserIds: "+ relatedUserIds);

        return repository.findAllByNickNameIn(relatedUserIds);
    }
    
}
