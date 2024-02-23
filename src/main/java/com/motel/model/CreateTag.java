package com.motel.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
//import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTag {
    @NotNull(message = "Tên nhãn rỗng")
    @NotEmpty(message = "Tên nhãn rỗng")
    // @Schema(description = "Tên nhãn",example="Beauty",required=true)
    private String title;
}
