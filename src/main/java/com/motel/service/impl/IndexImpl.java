package com.motel.service.impl;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.Indexs;
import com.motel.entity.Invoice;
import com.motel.entity.InvoiceStatus;
import com.motel.entity.MotelRoom;
import com.motel.model.IndexsModel;
import com.motel.repository.IndexsRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.IndexsService;

@Service
public class IndexImpl implements IndexsService {

    @Autowired
    IndexsRepository indexsRepository;

    @Autowired
    MotelRoomRepository motelRoomRepository;

    @Override
    public Indexs addIndexs(IndexsModel indexsModel) {
        Indexs indexs = new Indexs();
        StringBuilder errorMessage = new StringBuilder();

        if (indexsModel.getElectricityIndex() == null) {
            errorMessage.append("Không tìm thấy chỉ số điện !\n");
        }

        if (indexsModel.getWaterIndex() == null) {
            errorMessage.append("Không tìm thấy chỉ số nước !\n");
        }

        if (indexsModel.getMotelRoomId() == null) {
            errorMessage.append("Không tìm thấy phòng !\n");
        }

        if (errorMessage.length() > 0) {
            throw new IllegalArgumentException(errorMessage.toString());
        }

        else {
            Calendar newCalendars = Calendar.getInstance();
            newCalendars.setTime(indexs.getCreateDate());
            int newYearInvoice = newCalendars.get(Calendar.YEAR);
            int newMonthInvoice = newCalendars.get(Calendar.MONTH);

            List<Indexs> currentMonthIndexs = indexsRepository.findAll()
                    .stream()
                    .filter(inv -> inv.getMotelRoom().getMotelRoomId().equals(indexsModel.getMotelRoomId()))
                    .filter(inv -> {
                        Calendar invoiceCalendar = Calendar.getInstance();
                        invoiceCalendar.setTime(inv.getCreateDate());
                        int invoiceYear = invoiceCalendar.get(Calendar.YEAR);
                        int invoiceMonth = invoiceCalendar.get(Calendar.MONTH);
                        return invoiceYear == newYearInvoice && invoiceMonth == newMonthInvoice;
                    })
                    .collect(Collectors.toList());

            if (!currentMonthIndexs.isEmpty()) {
                throw new IllegalArgumentException("Chỉ số đã tạo tháng này rồi !");
            }

            List<Indexs> currentIndexsElectricWater = indexsRepository.findAll()
                    .stream()
                    .filter(inv -> inv.getMotelRoom().getMotelRoomId().equals(indexsModel.getMotelRoomId()))
                    .filter(inv -> {
                        Double electricityIndexCurrent = inv.getElectricityIndex();
                        Double waterIndexCurrent = inv.getWaterIndex();
                        return (electricityIndexCurrent == indexsModel.getElectricityIndex()
                                || electricityIndexCurrent > indexsModel.getElectricityIndex())
                                || (waterIndexCurrent == indexsModel.getWaterIndex()
                                        || waterIndexCurrent > indexsModel.getWaterIndex());
                    })
                    .collect(Collectors.toList());

            if (!currentIndexsElectricWater.isEmpty()) {
                throw new IllegalArgumentException("Chỉ số đang tạo phải lớn hơn tháng trước !");
            }

            if (indexsModel.getElectricityIndex() < 0) {
                throw new IllegalArgumentException("Chỉ số điện đang tạo phải lớn hơn 0 !");
            }

            if (indexsModel.getWaterIndex() < 0) {
                throw new IllegalArgumentException("Chỉ số nước đang tạo phải lớn hơn 0 !");
            }

            indexs.setCreateDate(new Date());
            indexs.setElectricityIndex(indexsModel.getElectricityIndex());
            indexs.setWaterIndex(indexsModel.getWaterIndex());

            MotelRoom motelRoom = motelRoomRepository.findById(indexsModel.getMotelRoomId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Invalid invoice status Id: " + indexsModel.getMotelRoomId()));
            indexs.setMotelRoom(motelRoom);
            indexsRepository.save(indexs);

            return indexs;
        }

    }

    @Override
    public Indexs updateIndexs(Integer indexsId, Indexs updatedIndexs) {
        Indexs existingIndexs = indexsRepository.findById(indexsId)
                .orElseThrow(() -> new IllegalArgumentException("Index with ID " + indexsId + " not found"));
        StringBuilder errorMessage = new StringBuilder();

        if (updatedIndexs.getElectricityIndex() == null) {
            errorMessage.append("Không tìm thấy chỉ số điện !\n");
        }

        if (updatedIndexs.getWaterIndex() == null) {
            errorMessage.append("Không tìm thấy chỉ số nước !\n");
        }

        if (errorMessage.length() > 0) {
            throw new IllegalArgumentException(errorMessage.toString());
        }

        List<Indexs> currentIndexsElectricWater = indexsRepository.findAll()
                .stream()
                .filter(inv -> inv.getMotelRoom().getMotelRoomId()
                        .equals(existingIndexs.getMotelRoom().getMotelRoomId()))
                .filter(inv -> {
                    Date currentDate = updatedIndexs.getCreateDate();
                    Date indexDate = inv.getCreateDate();
                    return indexDate.compareTo(currentDate) < 0;
                })
                .filter(inv -> {
                    Double electricityIndexCurrent = inv.getElectricityIndex();
                    Double waterIndexCurrent = inv.getWaterIndex();
                    return (electricityIndexCurrent == updatedIndexs.getElectricityIndex()
                            || electricityIndexCurrent > updatedIndexs.getElectricityIndex())
                            || (waterIndexCurrent == updatedIndexs.getWaterIndex()
                                    || waterIndexCurrent > updatedIndexs.getWaterIndex());
                })
                .collect(Collectors.toList());

        if (!currentIndexsElectricWater.isEmpty()) {
            throw new IllegalArgumentException("Chỉ số đang tạo phải lớn hơn tháng trước !");
        }

        if (updatedIndexs.getElectricityIndex() < 0) {
            throw new IllegalArgumentException("Chỉ số điện đang tạo phải lớn hơn 0 !");
        }

        if (updatedIndexs.getWaterIndex() < 0) {
            throw new IllegalArgumentException("Chỉ số nước đang tạo phải lớn hơn 0 !");
        }

        existingIndexs.setElectricityIndex(updatedIndexs.getElectricityIndex());
        existingIndexs.setWaterIndex(updatedIndexs.getWaterIndex());

        Indexs savedIndexs = indexsRepository.save(existingIndexs);
        return savedIndexs;
    }

    @Override
    public Optional<Indexs> getById(Integer indexsId) {
        return indexsRepository.findById(indexsId);
    }

    @Override
    public Indexs addIndexsOrigin() {
        Indexs indexs = new Indexs();
        List<MotelRoom> motelRoom = motelRoomRepository.findMotelRoomByIndex();

        indexs.setElectricityIndex(0.0);
        indexs.setWaterIndex(0.0);
        indexs.setCreateDate(new Date());
        indexs.setMotelRoom(motelRoom.get(0));

        Indexs savedIndexs = indexsRepository.save(indexs);
        return savedIndexs;
    }

    @Override
    @Transactional
    public void deleteIndexs(Integer motelRoomId) {
        indexsRepository.deleteIndexByMotelRoom(motelRoomId);
        // this.addIndexsOrigin();
    }

    @Override
    public void deleteIndexes(Integer indexesId) {
        indexsRepository.deleteById(indexesId);
    }

    @Override
    public List<Indexs> findIndexsByMotelRoom(Integer motelRoomId) {
        return indexsRepository.getIndexsByMotelRoom(motelRoomId);
    }

}
