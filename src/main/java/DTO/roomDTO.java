package DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class roomDTO {
	private String image;
    private String address;
    private String ward;
    private double price;
    private double area;
    private String title;
    private String fullname;
    private Date createDate;
    private String district;
    private String city;
}
