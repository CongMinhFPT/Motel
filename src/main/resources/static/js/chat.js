'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let nickname = null;
let fullname = null;
let avatar = null;
let selectedUserId = null;
let chatuser = null;

function cleanNickname(nickname) {
	return nickname.replace(/[^a-zA-Z0-9]/g, '_');
}

function cleanChatuser(chatuser) {
	return chatuser.replace(/[^a-zA-Z0-9]/g, '_');
}


function connect(event) {
	nickname = cleanNickname(document.querySelector('#nickname').value.trim());
	fullname = document.querySelector('#fullname').value.trim();
	avatar = document.querySelector('#avatar').value.trim();
	chatuser = cleanChatuser(document.querySelector('#chatuser').value.trim());

	if (!avatar) {
		avatar = 'user_icon.png';
	}
	if (nickname && fullname && avatar) {
		usernamePage.classList.add('hidden');
		chatPage.classList.remove('hidden');

		const socket = new SockJS('/ws');
		stompClient = Stomp.over(socket);

		stompClient.connect({}, onConnected, onError);

	}
	event.preventDefault();
}


function onConnected() {
	stompClient.subscribe(`/user/${nickname}/queue/messages`, onMessageReceived);
	stompClient.subscribe(`/user/public`, onMessageReceived);

	if (!avatar) {
		avatar = 'user_icon.png';
	}
	// register the connected user
	stompClient.send("/app/user.addUser",
		{},
		JSON.stringify({ nickName: nickname, fullName: fullname, avatar: avatar, status: 'ONLINE' })
	);
	document.querySelector('#connected-user-fullname').textContent = fullname;
	findAndDisplayConnectedUsers().then();
}

function autoClick(elementId) {
	const element = document.getElementById(elementId);
	if (element) {
		element.click();
	}
}


async function findAndDisplayConnectedUsers() {
	const connectedUsersResponse = await fetch(`/users/messages/${nickname}`);
	let connectedUsers = await connectedUsersResponse.json();
	connectedUsers = connectedUsers.filter(user => user.nickName !== nickname);

	const connectedUsersResponse1 = await fetch(`/allUser/${nickname}`);
	let connectedUsers1 = await connectedUsersResponse1.json();
	connectedUsers1 = connectedUsers1.filter(user1 => user1.nickName !== nickname);
	const connectedUsersList = document.getElementById('connectedUsers');
	connectedUsersList.innerHTML = '';


	connectedUsers1.forEach(user1 => {
		if (user1.nickName == chatuser) {
			appendUserElement(user1, connectedUsersList);
			const separator = document.createElement('li');
			separator.classList.add('separator');
			connectedUsersList.appendChild(separator);
			autoClick(user1.nickName);
		} else {
			if (chatuser == 'null') {
				return;
			}
			Swal.fire({
				icon: 'error',
				title: 'Người dùng chưa sử dụng chat',
				text: 'Người dùng ' + chatuser + ' hiện chưa sử dụng chat.',
			});
			return;
		}
	});
	connectedUsers = connectedUsers.filter(user => user.nickName !== chatuser);
	connectedUsers.forEach(user => {
		appendUserElement(user, connectedUsersList);
		if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
			const separator = document.createElement('li');
			separator.classList.add('separator');
			connectedUsersList.appendChild(separator);
		}
	});
}
function appendUserElement(user, connectedUsersList) {
	const listItem = document.createElement('li');
	listItem.classList.add('user-item');
	listItem.id = user.nickName;

	const userImage = document.createElement('img');
	userImage.src = user.avatar ? '/img/' + user.avatar : '/img/user_icon.png';
	
	 const userInfoContainer = document.createElement('div');
    userInfoContainer.classList.add('user-info');

	const usernameSpan = document.createElement('span');
	usernameSpan.textContent = user.fullName;
	 usernameSpan.classList.add('username');
	
		const usernameStatus = document.createElement('span');
	usernameStatus.textContent = user.status;
	  usernameStatus.classList.add('status');

	const receivedMsgs = document.createElement('span');
	receivedMsgs.textContent = '0';
	receivedMsgs.classList.add('nbr-msg', 'hidden');

	   listItem.appendChild(userImage);
    userInfoContainer.appendChild(usernameSpan);
    userInfoContainer.appendChild(usernameStatus);
    listItem.appendChild(userInfoContainer);
    listItem.appendChild(receivedMsgs);
	
  if (user.status === 'ONLINE') {
        const statusDot = document.createElement('span');
        statusDot.classList.add('status-dot', 'online');
        usernameStatus.prepend(statusDot); 
    }
	listItem.addEventListener('click', userItemClick);

	connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
	document.querySelectorAll('.user-item').forEach(item => {
		item.classList.remove('active');
	});
	messageForm.classList.remove('hidden');

	const clickedUser = event.currentTarget;
	clickedUser.classList.add('active');

	selectedUserId = clickedUser.getAttribute('id');
	fetchAndDisplayUserChat().then();

	const nbrMsg = clickedUser.querySelector('.nbr-msg');
	nbrMsg.classList.add('hidden');
	nbrMsg.textContent = '0';

}

function displaySelectedImage(event) {
    const file = event.target.files[0];
    if (file) {
        const fileName = file.name;
        const selectedImageContainer = document.getElementById('selectedImageContainer');
        selectedImageContainer.innerHTML = '';
        const imagePath = '/chat/' + fileName; 
        const newImage = document.createElement('img');
        newImage.src = imagePath;
        selectedImageContainer.appendChild(newImage);
        selectedImageContainer.classList.remove('hidden');
        
        const closeButton = document.createElement('span');
        closeButton.textContent = 'x';
        closeButton.classList.add('close-button');
        closeButton.addEventListener('click', function() {
            clearSelectedImage();
            document.getElementById('fileInput').value = ''; 
        });
        selectedImageContainer.appendChild(closeButton);
        closeButton.addEventListener('mouseenter', function() {
            selectedImageContainer.classList.add('show-cursor');
        });
        closeButton.addEventListener('mouseleave', function() {
            selectedImageContainer.classList.remove('show-cursor');
        });
    }
}

function displayMessage(senderId, content, fileName) {
	const messageContainer = document.createElement('div');
	messageContainer.classList.add('message');
	if (senderId === nickname) {
		messageContainer.classList.add('sender');
	} else {
		messageContainer.classList.add('receiver');
	}

	let message = null;

	if (content) {
		message = document.createElement('p');
		message.textContent = content;
		messageContainer.appendChild(message);
	}

	if (fileName != null && content == '') {
		const imageContainer = document.createElement('div');
		imageContainer.classList.add('image-container');
		const image = document.createElement('img');
		image.src = `/chat/${fileName}`;
		imageContainer.appendChild(image);
		messageContainer.appendChild(imageContainer);

		if (message) {
			messageContainer.removeChild(message);
		}
	} else if (fileName != null && content != '') {
		const imageContainer = document.createElement('div');
		imageContainer.classList.add('image-container');
		const image = document.createElement('img');
		image.src = `/chat/${fileName}`;
		imageContainer.appendChild(image);
		messageContainer.appendChild(imageContainer);
	}

	chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
	const userChatResponse = await fetch(`/messages/${nickname}/${selectedUserId}`);
	const userChat = await userChatResponse.json();
	chatArea.innerHTML = '';
	userChat.forEach(chat => {
		displayMessage(chat.senderId, chat.content, chat.fileName);
	});
	chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}

function clearSelectedImage() {
    const selectedImageContainer = document.getElementById('selectedImageContainer');
    selectedImageContainer.innerHTML = '';
    selectedImageContainer.classList.add('hidden');
}

function sendMessage(event) {
	const messageContent = messageInput.value.trim();	
	const file = document.getElementById('fileInput').files[0];
	if ((messageContent || file) && stompClient) {
		const chatMessage = {
			senderId: nickname,
			recipientId: selectedUserId,
			content: messageInput.value.trim(),
			timestamp: new Date()
		};

		if (file) {
			chatMessage.fileName = file.name;
			stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
			displayMessage(nickname, messageInput.value.trim(), chatMessage.fileName);
			messageInput.value = '';
			document.getElementById('fileInput').value = '';
			    clearSelectedImage(); 
			
		} else {
			stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
			displayMessage(nickname, messageInput.value.trim());
			messageInput.value = '';
		}
	}
clearSelectedImage();
	chatArea.scrollTop = chatArea.scrollHeight;
	event.preventDefault();
}

async function onMessageReceived(payload) {
	await findAndDisplayConnectedUsers();
	console.log('Message received', payload);
	const message = JSON.parse(payload.body);
	if (selectedUserId && selectedUserId === message.senderId) {
		displayMessage(message.senderId, message.content, message.fileName);
		chatArea.scrollTop = chatArea.scrollHeight;
	}

	if (selectedUserId) {
		document.querySelector(`#${selectedUserId}`).classList.add('active');
	} else {
		messageForm.classList.add('hidden');
	}

	const notifiedUser = document.querySelector(`#${message.senderId}`);
	if (notifiedUser && !notifiedUser.classList.contains('active')) {
		const nbrMsg = notifiedUser.querySelector('.nbr-msg');
		nbrMsg.classList.remove('hidden');
		nbrMsg.textContent = '';
	}
}

function onLogout() {
	stompClient.send("/app/user.disconnectUser",
		{},
		JSON.stringify({ nickName: nickname, fullName: fullname, status: 'OFFLINE' })
	);
	window.location.reload();
}

usernameForm.addEventListener('submit', connect, true); // step 1
messageForm.addEventListener('submit', sendMessage, true);
window.onbeforeunload = () => onLogout();

