package DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class roomDTO {
	private String image;
    private String address;
    private int price;
    private double area;
    private String category;
    private String ownerName;
    private LocalDate createDate;
    private String district;
    private String province;
}
