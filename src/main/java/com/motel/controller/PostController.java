package com.motel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.motel.entity.Account;
import com.motel.entity.ElectricityCash;
import com.motel.entity.Image;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.entity.Post;
import com.motel.entity.PostMotel;
import com.motel.entity.RoomCash;
import com.motel.entity.WaterCash;
import com.motel.entity.WifiCash;
import com.motel.repository.AccountsRepository;
import com.motel.repository.ElectricityCashRepository;
import com.motel.repository.ImageRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.PostRepository;
import com.motel.repository.RoomCashRepository;
import com.motel.repository.WaterCashRepository;
import com.motel.repository.WifiCashRepository;
import com.motel.service.GeocodingService;
import com.motel.service.PostService;

@Controller
public class PostController {
	@Autowired
	MotelRepository motelRepository;
	@Autowired
	PostRepository postRepository;

	@Autowired
	RoomCashRepository roomCashRepository;

	@Autowired
	WifiCashRepository wifiCashRepository;

	@Autowired
	ElectricityCashRepository electricityCashRepository;

	@Autowired
	WaterCashRepository waterCashRepository;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	MotelRoomRepository motelRoomRepository;

	@Autowired
	private GeocodingService geocodingService;

	@Autowired
	AccountsRepository accountsRepository;

	@GetMapping("/room-details/motel-{motel_room_id}")
	public String showRoomDetals(@PathVariable("motel_room_id") Integer motel_room_id, Model model,
			Authentication authentication) {
		// Post post = postRepository.findById(postId).orElse(null);

		if (authentication == null) {
			String emailAccount = null;
			Account account = accountsRepository.getByEmail(emailAccount);
			model.addAttribute("accountIdFavorite", account == null ? null : account.getAccountId());
		} else {
			String emailAccount = authentication.getName();
			Account account = accountsRepository.getByEmail(emailAccount);
			model.addAttribute("accountIdFavorite", account.getAccountId());
		}

		MotelRoom motelRoom = motelRoomRepository.findById(motel_room_id).orElse(null);
		if (motelRoom != null) {
			model.addAttribute("motelRoom", motelRoom);
			model.addAttribute("roomCash", roomCash(motel_room_id));
			model.addAttribute("roomWifi", roomWifi(motel_room_id));
			model.addAttribute("roomWater", roomWater(motel_room_id));
			model.addAttribute("roomElectricity", roomElectricity(motel_room_id));

			String address = motelRoom.getMotel().getDetailAddress() + ", "
					+ motelRoom.getMotel().getWard() + ", " + motelRoom.getMotel().getDistrict()
					+ ", " + motelRoom.getMotel().getProvince();
			double[] coordinates = geocodingService.getCoordinates(address);
			if (coordinates != null && coordinates.length == 2) {
				double latitude = coordinates[0];
				double longitude = coordinates[1];
				model.addAttribute("latitude", latitude);
				model.addAttribute("longitude", longitude);

			}
			MotelRoom SameArea = motelRoomRepository
					.findMotelRoomByMotelIdAndMotelRoomId(motelRoom.getMotel().getMotelId(), motel_room_id);
			Iterable<Image> images = imageRepository.findByMotelRoom_MotelRoomId(motelRoom.getMotelRoomId());
			model.addAttribute("images", images);

			Iterable<Post> sameDistrictAndProvincePosts = postRepository
					.findByMotel_DistrictAndMotel_Province(
							motelRoom.getMotel().getDistrict(), motelRoom.getMotel().getProvince());
			Post postId = postRepository.findPostByMotelId(motelRoom.getMotel().getMotelId());
			List<Post> filteredPosts = new ArrayList<>();
			for (Post p : sameDistrictAndProvincePosts) {
				if (!p.getPostId().equals(postId.getPostId())) {
					filteredPosts.add(p);
				}
			}
			model.addAttribute("sameDistrictAndProvincePosts", filteredPosts);
			List<Double> roomBills = new ArrayList<>();
			List<String> roomImage = new ArrayList<>();
			for (Post sameDistrictAndProvincePost : filteredPosts) {
				Motel motel = sameDistrictAndProvincePost.getMotel();
				MotelRoom motel_room = motelRoomRepository.findMotelRoomByMotelId(motel.getMotelId());
				RoomCash roomCash = roomCashRepository.findByMotelId(motel_room.getMotelRoomId());
				Iterable<Image> images1 = imageRepository.findByMotelRoom_MotelRoomId(motel_room.getMotelRoomId());
				if (roomCash != null && images1 != null) {
					roomBills.add(roomCash.getRoomBill());
					Image firstImage = StreamSupport.stream(images1.spliterator(), false).findFirst().orElse(null);
					if (firstImage != null) {
						roomImage.add(firstImage.getName());
					}
				}
			}

			model.addAttribute("roomBills", roomBills);
			model.addAttribute("roomImage", roomImage);

			return "room/room_detail";
		} else {
			return "error";
		}
	}

	@ModelAttribute("motelRoom")
	public MotelRoom motelRoom(@PathVariable("motel_room_id") Integer motel_room_id) {
		MotelRoom motelRoom = motelRoomRepository.findById(motel_room_id).get();
		return motelRoom;
	}

	@ModelAttribute("roomCash")
	public RoomCash roomCash(@PathVariable("motel_room_id") Integer motel_room_id) {
		MotelRoom motelRoom = motelRoomRepository.findById(motel_room_id).orElse(null);
		if (motelRoom != null) {
			return roomCashRepository.findByMotelId(motelRoom.getMotelRoomId());
		}
		return null;
	}

	@ModelAttribute("roomWifi")
	public WifiCash roomWifi(@PathVariable("motel_room_id") Integer motel_room_id) {
		MotelRoom motelRoom = motelRoomRepository.findById(motel_room_id).orElse(null);
		if (motelRoom != null) {
			return wifiCashRepository.findByMotelIdOfWifiCash(motelRoom.getMotelRoomId());
		}
		return null;
	}

	@ModelAttribute("roomElectricity")
	public ElectricityCash roomElectricity(@PathVariable("motel_room_id") Integer motel_room_id) {
		MotelRoom motelRoom = motelRoomRepository.findById(motel_room_id).orElse(null);
		if (motelRoom != null) {
			return electricityCashRepository.findByMotelIdOfElectricityCash(motelRoom.getMotelRoomId());
		}
		return null;
	}

	@ModelAttribute("roomWater")
	public WaterCash roomWater(@PathVariable("motel_room_id") Integer motel_room_id) {
		MotelRoom motelRoom = motelRoomRepository.findById(motel_room_id).orElse(null);
		if (motelRoom != null) {
			return waterCashRepository.findByMotelIdOfWaterCash(motelRoom.getMotelRoomId());
		}
		return null;
	}
}
