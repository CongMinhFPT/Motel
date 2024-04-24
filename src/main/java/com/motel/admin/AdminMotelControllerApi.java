package com.motel.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.ElectricityCash;
import com.motel.entity.MotelRoom;
import com.motel.entity.MotelRoomNew;
import com.motel.entity.RoomCash;
import com.motel.entity.RoomStatus;
import com.motel.entity.WaterCash;
import com.motel.entity.WifiCash;
import com.motel.repository.ElectricityCashRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.RoomCashRepository;
import com.motel.repository.RoomStatusRepository;
import com.motel.repository.WaterCashRepository;
import com.motel.repository.WifiCashRepository;
import com.motel.service.MotelRoomService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
public class AdminMotelControllerApi {
    @Autowired
    MotelRepository motelRepository;
    @Autowired
    MotelRoomRepository motelRoomRepository;
    @Autowired
    MotelRoomService motelRoomService;
    @Autowired
    WaterCashRepository waterCashRepository;
    @Autowired
    WifiCashRepository wifiCashRepository;
    @Autowired
    ElectricityCashRepository electricityCashRepository;
    @Autowired
    RoomCashRepository roomCashRepository;
    @Autowired
    RoomStatusRepository roomStatusRepository;

    @GetMapping("/api/all/MotelRoom/{id}")
    public ResponseEntity<List<MotelRoomNew>> GetAllMotelRoom(@PathVariable int id) {
        List<MotelRoom> listMotelRooms = motelRepository.getById(id).getMotelRoom();
        List<MotelRoomNew> motelRoomNews = new ArrayList<>();
        listMotelRooms.forEach(a -> {
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
                    a.getCategoryRoom().getQuantity(),
                    a.getRoomStatus() != null ? a.getRoomStatus().getName() : null);
            motelRoomNews.add(motelRoomNew);
        });
        return ResponseEntity.ok(motelRoomNews);
    }

    @GetMapping("/api/all/motel/getallroomstatus/{idmotel}")
    public ResponseEntity<Map<String, Object>> GetAllRoomStatus(@PathVariable int idmotel) {
        List<RoomStatus> listStatus = roomStatusRepository.findAll();
        MotelRoom motelRoom = motelRoomRepository.getById(idmotel);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("listStatus", listStatus);
        map.put("roomStatus", motelRoom.getRoomStatus());
        map.put("number", motelRoom.getRenter().size());
        map.put("datestatus", motelRoom.getCheckoutdate());
        return ResponseEntity.ok(map);
    }

    @GetMapping("/api/all/Motel/{idmotel}/BilliMotelRoom/{id}")
    public ResponseEntity<Map<String, Object>> GetAllBillMotelRoom(@PathVariable int idmotel, @PathVariable int id) {
        if (motelRoomService.CheckMotelRoomInMotel(idmotel, id)) {
            MotelRoom motelRoom = motelRoomRepository.getById(id);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ElectricityCash", motelRoom.getElectricityCash());
            map.put("WaterCash", motelRoom.getWaterCash());
            map.put("WifiCash", motelRoom.getWifiCash());
            map.put("RoomCash", motelRoom.getRoomCash());
            return ResponseEntity.ok(map);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/bill/{namebill}/motel/{idmotel}/motelroom/{idmotelroom}")
    public @ResponseBody ResponseEntity<Void> PostAllBillMotelRoom(@RequestBody Double data,
            @PathVariable String namebill, @PathVariable int idmotel, @PathVariable int idmotelroom) {
        if (motelRoomService.CheckMotelRoomInMotel(idmotel, idmotelroom)) {
            MotelRoom motelRoom = motelRoomRepository.getById(idmotelroom);
            switch (namebill) {
                case "dien":
                    if (motelRoom.getElectricityCash().size() != 0) {
                        ElectricityCash electricityCash = motelRoom.getElectricityCash().get(0);
                        electricityCash.setElectricityBill(data);
                        electricityCashRepository.save(electricityCash);
                    } else {
                        ElectricityCash electricityCash = new ElectricityCash();
                        electricityCash.setElectricityBill(data);
                        electricityCash.setMotelRoom(motelRoom);
                        electricityCashRepository.save(electricityCash);
                    }
                    break;
                case "nuoc":
                    if (motelRoom.getWaterCash().size() != 0) {
                        WaterCash waterCash = motelRoom.getWaterCash().get(0);
                        waterCash.setWaterBill(data);
                        waterCashRepository.save(waterCash);
                    } else {
                        WaterCash waterCash = new WaterCash();
                        waterCash.setWaterBill(data);
                        waterCash.setMotelRoom(motelRoom);
                        waterCashRepository.save(waterCash);
                    }
                    break;
                case "wifi":
                    if (motelRoom.getWifiCash().size() != 0) {
                        WifiCash wifiCash = motelRoom.getWifiCash().get(0);
                        wifiCash.setWifiBill(data);
                        wifiCashRepository.save(wifiCash);
                    } else {
                        WifiCash wifiCash = new WifiCash();
                        wifiCash.setWifiBill(data);
                        wifiCash.setMotelRoom(motelRoom);
                        wifiCashRepository.save(wifiCash);
                    }
                    break;
                case "giaphong":
                    if (motelRoom.getRoomCash().size() != 0) {
                        RoomCash roomCash = motelRoom.getRoomCash().get(0);
                        roomCash.setRoomBill(data);
                        roomCashRepository.save(roomCash);
                    } else {
                        RoomCash roomCash = new RoomCash();
                        roomCash.setRoomBill(data);
                        roomCash.setMotelRoom(motelRoom);
                        roomCashRepository.save(roomCash);
                    }
                    break;

                default:
                    return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("api/motel/roomstatus/{idmotelroom}/{idstatus}")
    public ResponseEntity<Void> postUpdataroomstatus(@PathVariable int idmotelroom, @PathVariable int idstatus,
            @RequestBody Date date) {
        MotelRoom entity = motelRoomRepository.getById(idmotelroom);
        RoomStatus roomStatus = roomStatusRepository.getById(idstatus);
        if (entity.getRoomStatus() != roomStatus) {
            entity.setRoomStatus(roomStatus);
            entity.setCheckoutdate(date);
            motelRoomRepository.save(entity);
            return ResponseEntity.ok().build();
        } else {
            entity.setCheckoutdate(date);
            motelRoomRepository.save(entity);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("api/motel/roomstatus/{idmotelroom}/{idstatus}")
    public ResponseEntity<Void> postUpdataroomstatus2(@PathVariable int idmotelroom, @PathVariable int idstatus) {
        MotelRoom motelRoom = motelRoomRepository.getById(idmotelroom);
        RoomStatus roomStatus = roomStatusRepository.getById(idstatus);
        if (motelRoom.getRenter().size() == 0) {
            motelRoom.setRoomStatus(roomStatus);
            motelRoom.setCheckoutdate(null);
            motelRoomRepository.save(motelRoom);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("api/motel/roomstatus2/{idmotelroom}/{idstatus}")
    public ResponseEntity<Void> postUpdataroomstatus3(@PathVariable int idmotelroom, @PathVariable int idstatus) {
        MotelRoom motelRoom = motelRoomRepository.getById(idmotelroom);
        RoomStatus roomStatus = roomStatusRepository.getById(idstatus);
        motelRoom.setCheckoutdate(null);
        motelRoom.setRoomStatus(roomStatus);
        motelRoomRepository.save(motelRoom);
        return ResponseEntity.ok().build();
    }

}
