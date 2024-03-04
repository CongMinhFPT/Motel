package com.motel.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBlog {

    @NotNull(message = "Hãy nhập tiêu đề ")
    @NotEmpty(message = "Hãy nhập tiêu đề")
    @Size(min = 5, max = 255, message = "Độ dài tiêu đề từ 1-255 ký tự ")
    private String title;

    @NotNull(message = "Hãy nhập mô tả")
    @NotEmpty(message = "Hãy nhập mô tả")
    @Size(min = 5, max = 255, message = "Độ dài mô tả từ 1-255 ký tự ")
    private String descriptions;

    @NotNull(message = "Ảnh đang rỗng")
    @NotEmpty(message = "Ảnh đang rỗng")
    private Integer imageId;

    private String fullname;
    private Set<Integer> tag = new HashSet<>();

}