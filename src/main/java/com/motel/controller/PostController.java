package com.motel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.motel.entity.Account;
import com.motel.entity.ElectricityCash;
import com.motel.entity.Image;
import com.motel.entity.MotelRoom;
import com.motel.entity.Post;
import com.motel.entity.RoomCash;
import com.motel.entity.WaterCash;
import com.motel.entity.WifiCash;
import com.motel.repository.AccountsRepository;
import com.motel.repository.ElectricityCashRepository;
import com.motel.repository.ImageRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.PostRepository;
import com.motel.repository.RoomCashRepository;
import com.motel.repository.WaterCashRepository;
import com.motel.repository.WifiCashRepository;
import com.motel.service.GeocodingService;

@Controller
public class PostController {

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
	

	@GetMapping("/room-details/{post_id}")
	public String showRoomDetals(@PathVariable("post_id") Integer postId, Model model) {
		Post post = postRepository.findById(postId).orElse(null);
		if (post != null) {
			model.addAttribute("roomDetails", post);
			model.addAttribute("roomCash", roomCash(postId));
			model.addAttribute("roomWifi", roomWifi(postId));
			model.addAttribute("roomWater", roomWater(postId));
			model.addAttribute("roomElectricity", roomElectricity(postId));
			
			String address = post.getMotelRoom().getMotel().getDetailAddress() + ", "
					+ post.getMotelRoom().getMotel().getWard() + ", " + post.getMotelRoom().getMotel().getDistrict()
					+ ", " + post.getMotelRoom().getMotel().getProvince();
			double[] coordinates = geocodingService.getCoordinates(address);
			if (coordinates != null && coordinates.length == 2) {
				double latitude = coordinates[0];
				double longitude = coordinates[1];
				model.addAttribute("latitude", latitude);
				model.addAttribute("longitude", longitude);

			}
			Integer motelRoomId = post.getMotelRoom().getMotelRoomId();
			Iterable<Image> images = imageRepository.findByMotelRoom_MotelRoomId(motelRoomId);
			model.addAttribute("images", images);

			Iterable<Post> sameDistrictAndProvincePosts = postRepository
					.findByMotelRoom_Motel_DistrictAndMotelRoom_Motel_Province(
							post.getMotelRoom().getMotel().getDistrict(), post.getMotelRoom().getMotel().getProvince());

			List<Post> filteredPosts = new ArrayList<>();
			for (Post p : sameDistrictAndProvincePosts) {
				if (!p.getPostId().equals(postId)) {
					filteredPosts.add(p);
				}
			}
			model.addAttribute("sameDistrictAndProvincePosts", filteredPosts);

			// RoomCash related accommodation
			List<Double> roomBills = new ArrayList<>();
			List<String> roomImage = new ArrayList<>();
			for (Post sameDistrictAndProvincePost : filteredPosts) {
				MotelRoom motelRoom = sameDistrictAndProvincePost.getMotelRoom();
				RoomCash roomCash = roomCashRepository.findByMotelId(motelRoom.getMotelRoomId());
				Iterable<Image> images1 = imageRepository.findByMotelRoom_MotelRoomId(motelRoom.getMotelRoomId());
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

			String firstImageName = getFirstImageName(post);
			model.addAttribute("firstImageName", firstImageName);
			return "room/room_detail";
		} else {
			return "error";
		}
	}

	private String getFirstImageName(Post post) {
		Integer motelRoomId = post.getMotelRoom().getMotelRoomId();
		Iterable<Image> images = imageRepository.findByMotelRoom_MotelRoomId(motelRoomId);
		for (Image image : images) {
			return image.getName();
		}
		return null;
	}

	@ModelAttribute("roomDetails")
	public Post roomDetails(@PathVariable("post_id") Integer post_id) {
		Post post = postRepository.findById(post_id).get();
		return post;
	}

	@ModelAttribute("roomCash")
	public RoomCash roomCash(@PathVariable("post_id") Integer postId) {
		Post post = postRepository.findById(postId).orElse(null);
		if (post != null) {
			Integer motelId = post.getMotelRoom().getMotelRoomId();
			return roomCashRepository.findByMotelId(motelId);
		}
		return null;
	}

	@ModelAttribute("roomWifi")
	public WifiCash roomWifi(@PathVariable("post_id") Integer postId) {
		Post post = postRepository.findById(postId).orElse(null);
		if (post != null) {
			Integer motelId = post.getMotelRoom().getMotelRoomId();
			return wifiCashRepository.findByMotelIdOfWifiCash(motelId);
		}
		return null;
	}

	@ModelAttribute("roomElectricity")
	public ElectricityCash roomElectricity(@PathVariable("post_id") Integer postId) {
		Post post = postRepository.findById(postId).orElse(null);
		if (post != null) {
			Integer motelId = post.getMotelRoom().getMotelRoomId();
			return electricityCashRepository.findByMotelIdOfElectricityCash(motelId);
		}
		return null;
	}

	@ModelAttribute("roomWater")
	public WaterCash roomWater(@PathVariable("post_id") Integer postId) {
		Post post = postRepository.findById(postId).orElse(null);
		if (post != null) {
			Integer motelId = post.getMotelRoom().getMotelRoomId();
			return waterCashRepository.findByMotelIdOfWaterCash(motelId);
		}
		return null;
	}



}
