package com.motel.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.MotelRoom;
import com.motel.entity.MotelRoomNew;
import com.motel.repository.MotelRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("*")
@RestController
public class AdminMotelControllerApi {
    @Autowired
    MotelRepository motelRepository;
    @GetMapping("/api/all/MotelRoom/{id}")
    public  ResponseEntity<List<MotelRoomNew>> GetAllMotelRoom(@PathVariable int id) {
        List<MotelRoom> listMotelRooms = motelRepository.getById(id).getMotelRoom();
        List<MotelRoomNew> motelRoomNews =new ArrayList<>();
        listMotelRooms.forEach(a->{
            List<String> name =new ArrayList<>();
            a.getImage().forEach(img->{
                name.add(img.getName());
            });
          MotelRoomNew motelRoomNew =new MotelRoomNew(a, name,a.getElectricityCash().isEmpty() ? null : a.getElectricityCash().get(0).getElectricityBill() 
          ,a.getWaterCash().isEmpty() ? null : a.getWaterCash().get(0).getWaterBill()
          ,a.getWifiCash().isEmpty() ? null : a.getWifiCash().get(0).getWifiBill()
          ,a.getWifiCash().isEmpty() ? null : a.getWifiCash().get(0).getPrice()
          ,a.getRoomCash().isEmpty() ? null : a.getRoomCash().get(0).getRoomBill()
          ,a.getRenter().isEmpty() ? 0 : a.getRoomCash().size()
          ,a.getCategoryRoom().getTitle()
          ,a.getRoomStatus()!=null ?a.getRoomStatus().getName():null);
          motelRoomNews.add(motelRoomNew);
        });
        return  ResponseEntity.ok(motelRoomNews);
    }
    
}
