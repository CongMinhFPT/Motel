package com.motel.restcontroller;

import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.Account;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.entity.MotelRoomNew;
import com.motel.entity.Post;
import com.motel.entity.PostMotel;
import com.motel.repository.MotelRepository;
import com.motel.repository.PostRepository;
@CrossOrigin("*") 
@RestController
public class PostMotelAPI {
    @Autowired
    PostRepository postRepository;

    @Autowired 
    MotelRepository motelRepository;

@GetMapping("/api/data/postmotel/{city}")
public ResponseEntity<List<PostMotel>> DataPostMotelHot(@PathVariable String city) {
    List<PostMotel> ListPostMotel = new ArrayList();
    List<Post> listpost = postRepository.findPosts(city);
   if (listpost.isEmpty()||listpost.size()<3) {
    List<Post> listpost2 = postRepository.findPostsnew();
    listpost2.removeAll(listpost);
    listpost.forEach(a ->{
        PostMotel motel = new PostMotel(a);
        ListPostMotel.add(motel);
    });
    listpost2.forEach(a ->{
        PostMotel motel = new PostMotel(a);
        ListPostMotel.add(motel);
    });
   }else{
    listpost.forEach(a ->{
        PostMotel motel = new PostMotel(a);
        ListPostMotel.add(motel);
    });
   }
   List<PostMotel> shuffled = ListPostMotel
   .stream()
   .sorted((e1, e2) -> Math.random() > 0.5 ? 1 : -1)
   .collect(Collectors.toList());
return  ResponseEntity.ok(shuffled);
}
/**
 * @return
 */
@GetMapping("/api/data/postmotel")
public ResponseEntity<List<PostMotel>> DataPostMoteldate() {
    List<PostMotel> ListPostMotel = new ArrayList();
    List<Post> listpost2 = postRepository.findPostsnew();
    listpost2.forEach(a ->{
        PostMotel motel = new PostMotel(a);
        ListPostMotel.add(motel);
    });
    List<PostMotel> shuffled = ListPostMotel
     .stream()
     .sorted((e1, e2) -> Math.random() > 0.5 ? 1 : -1)
     .collect(Collectors.toList());
return  ResponseEntity.ok(shuffled);
}

@GetMapping("/api/motel/motelroom/data/{motelid}")
public ResponseEntity<Map<String , Object>> GetDataMotelroomInMotel(@PathVariable Integer motelid) {
   Motel motel = motelRepository.getById(motelid);
   List<MotelRoomNew> motelRoomNews =new ArrayList<>();
   List<MotelRoom> list =motel.getMotelRoom();
   Account account =new Account();
   account = motel.getAccount();
   if (account.getEmail()!=null) {
    account.setPassword(null);
    account.setCitizen(null);
   }

   List<MotelRoom> roomsWithStatusTrue = list.stream()
    .filter(room -> room.isStatus() == true)
    .collect(Collectors.toList());
    roomsWithStatusTrue.forEach(a -> {
        List<String> name = new ArrayList<>();
        a.getImage().forEach(img -> {
            name.add(img.getName());
        });
        MotelRoomNew motelRoomNew = new MotelRoomNew(a, name,
                a.getElectricityCash().isEmpty() ? null : a.getElectricityCash().get(0).getElectricityBill(),
                a.getWaterCash().isEmpty() ? null : a.getWaterCash().get(0).getWaterBill(),
                a.getWifiCash().isEmpty() ? null : a.getWifiCash().get(0).getWifiBill(),
                a.getRoomCash().isEmpty() ? null : a.getRoomCash().get(0).getRoomBill(),
                a.getRenter().isEmpty() ? 0 : a.getRenter().size(),
                a.getCategoryRoom().getQuantity()+" Người, "+ a.getCategoryRoom().getDescriptions(),
                a.getRoomStatus() != null ? a.getRoomStatus().getName() : null);
        motelRoomNews.add(motelRoomNew);
    });
   Map<String, Object> map = new HashMap<String, Object>();
   map.put("listmotel", motelRoomNews);
   map.put("account", account);

return  ResponseEntity.ok(map);
}


}
