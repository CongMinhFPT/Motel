package com.motel.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenterModel {
    Integer accountId;
    Integer motelRoomId;
    Date renterDate;
}
