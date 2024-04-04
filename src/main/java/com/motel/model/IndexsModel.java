package com.motel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexsModel {
    Double electricityIndex;
    Double waterIndex;
    Integer motelRoomId;
}
