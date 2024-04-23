package com.motel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.motel.entity.ChatMessage;
import com.motel.entity.Status;
import com.motel.entity.User;
import com.motel.repository.ChatMessageRepository;
import com.motel.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	public List<User> findAllUsersWithMessages(String nickname) {
		List<String> allUserNicknames = repository.findAll().stream().map(User::getNickName)
				.collect(Collectors.toList());
		allUserNicknames.remove(nickname);
		return repository.findAllByNickNameIn(allUserNicknames);
	}

	public List<User> findUsersWithMessages(String nickname) {
		Set<String> relatedUserIds = new HashSet<>();

		List<ChatMessage> chatMessages = chatMessageRepository.findAll();
		for (ChatMessage chatMessage : chatMessages) {
			if (chatMessage.getSenderId().equals(nickname) || chatMessage.getRecipientId().equals(nickname)) {
				relatedUserIds.add(chatMessage.getSenderId().equals(nickname) ? chatMessage.getRecipientId()
						: chatMessage.getSenderId());
			}
		}
		System.out.println("relatedUserIds: " + relatedUserIds);
		List<String> relatedUserIdsList = new ArrayList<>(relatedUserIds);
		List<User> users = repository.findAllByNickNameIn(relatedUserIdsList);

		if (users.isEmpty()) {
			System.out.println("Người dùng " + nickname + " chưa từng nhắn tin với bất kỳ ai.");
			return new ArrayList<>();
		}

		return users;
	}
}
