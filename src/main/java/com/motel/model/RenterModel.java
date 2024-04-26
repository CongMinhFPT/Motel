package com.motel.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenterModel {
    Integer accountId;
    Integer motelRoomId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date renterDate;
}
