package com.motel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceModel {
    Integer motelRoomId;
    Integer invoiceStatusId;
}
