package com.motel.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MotelRoomsByPost {
    MotelRoom motelRoom1;
    Motel motel;

    public MotelRoomsByPost(MotelRoom motelRoom) {
        Motel motel2 = new Motel();
        List<MotelRoom> list = new ArrayList<>();
        this.motelRoom1 = motelRoom;
        this.motel = motelRoom.getMotel();
        // System.out.println(motelRoom.getMotel().getDescriptions());
        this.motelRoom1.setMotel(motel2);
        this.motel.setMotelRoom(list);
    }
}
