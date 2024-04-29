package com.motel.service;

import java.util.List;
import java.util.Optional;

import com.motel.entity.Indexs;
import com.motel.model.IndexsModel;

public interface IndexsService {
    Indexs addIndexs(IndexsModel indexsModel);

    Indexs updateIndexs(Integer indexsId, Indexs indexs);

    void deleteIndexs(Integer indexsId);

    void deleteIndexes(Integer indexesId);

    Indexs addIndexsOrigin();

    Optional<Indexs> getById(Integer indexsId);

    List<Indexs> findIndexsByMotelRoom(Integer motelRoomId);
}
