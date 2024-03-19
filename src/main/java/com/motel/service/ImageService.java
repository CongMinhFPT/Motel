package com.motel.service;

import java.util.List;

import com.motel.entity.Image;

public interface ImageService {
    List<Image> getListImage();
    
    Image getImageById (Integer imageId);

    Image save (Image image);

    List<Image> getListByAcount(Integer accountId);
    void deleteImage(Integer imageId);
  
} 
