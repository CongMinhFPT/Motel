package com.motel.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.motel.entity.ElectricityCash;
import com.motel.entity.Indexs;
import com.motel.entity.Invoice;
import com.motel.entity.InvoiceStatus;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.entity.RoomCash;
import com.motel.entity.WaterCash;
import com.motel.entity.WifiCash;
import com.motel.model.InvoiceModel;
import com.motel.repository.ElectricityCashRepository;
import com.motel.repository.IndexsRepository;
import com.motel.repository.InvoiceRepository;
import com.motel.repository.InvoiceStatusRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.RenterRepository;
import com.motel.repository.RoomCashRepository;
import com.motel.repository.WaterCashRepository;
import com.motel.repository.WifiCashRepository;
import com.motel.service.InvoiceService;

@Service
public class InvoiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    MotelRoomRepository motelRoomRepository;

    @Autowired
    IndexsRepository indexsRepository;

    @Autowired
    WaterCashRepository waterCashRepository;

    @Autowired
    RoomCashRepository roomCashRepository;

    @Autowired
    ElectricityCashRepository electricityCashRepository;

    @Autowired
    WifiCashRepository wifiCashRepository;

    @Autowired
    InvoiceStatusRepository invoiceStatusRepository;

    @Override
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice addInvoice(InvoiceModel invoiceModel) {
        Invoice invoice = new Invoice();
        StringBuilder errorMessage = new StringBuilder();

        Integer renterId = invoiceModel.getRenterId();
        if (renterId != null) {
            Renter renter = renterRepository.findById(renterId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid renter Id: " + renterId));
            MotelRoom motelRoom = renter.getMotelRoom();

            List<Indexs> oldIndexs = indexsRepository.findByMotelRoomIdOrderByMonth(motelRoom.getMotelRoomId());
            List<Indexs> newIndexs = indexsRepository.findByMotelRoomIdOrderByMonth(motelRoom.getMotelRoomId());

            WaterCash waterCash = waterCashRepository.findByMotelRoomId(motelRoom.getMotelRoomId());
            ElectricityCash electricityCash = electricityCashRepository.findByMotelRoomId(motelRoom.getMotelRoomId());
            RoomCash roomCash = roomCashRepository.findByMotelRoomId(motelRoom.getMotelRoomId());
            WifiCash wifiCash = wifiCashRepository.findByMotelRoomId(motelRoom.getMotelRoomId());

            if (waterCash == null) {
                errorMessage.append("Dữ liệu về tiền nước không tồn tại hoặc không được cập nhật.\n");
            }

            if (electricityCash == null) {
                errorMessage.append("Dữ liệu về tiền điện không tồn tại hoặc không được cập nhật.\n");
            }

            if (roomCash == null) {
                errorMessage.append("Dữ liệu về tiền phòng không tồn tại hoặc không được cập nhật.\n");
            }

            if (errorMessage.length() > 0) {
                throw new IllegalArgumentException(errorMessage.toString());
            }

            if (newIndexs != null && !newIndexs.isEmpty() && oldIndexs != null && !oldIndexs.isEmpty()) {

                if (newIndexs.size() > 0 && oldIndexs.size() > 1 && newIndexs.get(0) != null
                        && !newIndexs.get(0).equals("")
                        && oldIndexs.get(1) != null && !oldIndexs.get(1).equals("")) {
                    Indexs newIndex = newIndexs.get(0);
                    Indexs oldIndex = oldIndexs.get(1);
                    Calendar newCalendar = Calendar.getInstance();
                    newCalendar.setTime(newIndex.getCreateDate());
                    int newYear = newCalendar.get(Calendar.YEAR);
                    int newMonth = newCalendar.get(Calendar.MONTH);
                    int newDay = newCalendar.get(Calendar.DATE);

                    Calendar oldCalendar = Calendar.getInstance();
                    oldCalendar.setTime(oldIndex.getCreateDate());
                    int oldYear = oldCalendar.get(Calendar.YEAR);
                    int oldMonth = oldCalendar.get(Calendar.MONTH);
                    int oldDay = oldCalendar.get(Calendar.DATE);

                    if (newYear == oldYear && newMonth > oldMonth) {
                        Calendar newCalendars = Calendar.getInstance();
                        newCalendars.setTime(invoice.getCreateDate());
                        int newYearInvoice = newCalendars.get(Calendar.YEAR);
                        int newMonthInvoice = newCalendars.get(Calendar.MONTH);

                        List<Invoice> currentMonthInvoices = invoiceRepository.findAll()
                                .stream()
                                .filter(inv -> inv.getRenter().getRenterId().equals(invoiceModel.getRenterId()))
                                .filter(inv -> {
                                    Calendar invoiceCalendar = Calendar.getInstance();
                                    invoiceCalendar.setTime(inv.getCreateDate());
                                    int invoiceYear = invoiceCalendar.get(Calendar.YEAR);
                                    int invoiceMonth = invoiceCalendar.get(Calendar.MONTH);
                                    return invoiceYear == newYearInvoice && invoiceMonth == newMonthInvoice;
                                })
                                .collect(Collectors.toList());

                        if (!currentMonthInvoices.isEmpty()) {
                            throw new IllegalArgumentException("Hóa đơn đã tạo tháng này rồi!");
                        }

                        Double newElectricityIndex = newIndex.getElectricityIndex();
                        Double oldElectricityIndex = oldIndex.getElectricityIndex();

                        Double newWaterIndex = newIndex.getWaterIndex();
                        Double oldWaterIndex = oldIndex.getWaterIndex();

                        invoice.setNewElectricityIndex(newElectricityIndex);
                        invoice.setOldElectricityIndex(oldElectricityIndex);
                        invoice.setNewWaterIndex(newWaterIndex);
                        invoice.setOldWaterIndex(oldWaterIndex);

                        Double electricityDifference = newElectricityIndex - oldElectricityIndex;
                        Double waterDifference = newWaterIndex - oldWaterIndex;

                        Double wifiBill = wifiCash.getWifiBill() != null ? wifiCash.getWifiBill() : 0.0;
                        Double total = roomCash.getRoomBill()
                                + (electricityDifference * electricityCash.getElectricityBill())
                                + (waterDifference * waterCash.getWaterBill()) + wifiBill;

                        invoice.setTotalPrice(total);
                        invoice.setCreateDate(new Date());
                    } else if (newYear > oldYear || (newYear == oldYear && newMonth < oldMonth)) {
                        Calendar newCalendars = Calendar.getInstance();
                        newCalendars.setTime(invoice.getCreateDate());
                        int newYearInvoice = newCalendars.get(Calendar.YEAR);
                        int newMonthInvoice = newCalendars.get(Calendar.MONTH);

                        List<Invoice> currentMonthInvoices = invoiceRepository.findAll()
                                .stream()
                                .filter(inv -> inv.getRenter().getRenterId().equals(invoiceModel.getRenterId()))
                                .filter(inv -> {
                                    Calendar invoiceCalendar = Calendar.getInstance();
                                    invoiceCalendar.setTime(inv.getCreateDate());
                                    int invoiceYear = invoiceCalendar.get(Calendar.YEAR);
                                    int invoiceMonth = invoiceCalendar.get(Calendar.MONTH);
                                    return invoiceYear == newYearInvoice && invoiceMonth == newMonthInvoice;
                                })
                                .collect(Collectors.toList());

                        if (!currentMonthInvoices.isEmpty()) {
                            throw new IllegalArgumentException("Hóa đơn đã tạo tháng này rồi!");
                        }

                        Double newElectricityIndex = newIndex.getElectricityIndex();
                        Double oldElectricityIndex = oldIndex.getElectricityIndex();

                        Double newWaterIndex = newIndex.getWaterIndex();
                        Double oldWaterIndex = oldIndex.getWaterIndex();

                        invoice.setNewElectricityIndex(newElectricityIndex);
                        invoice.setOldElectricityIndex(oldElectricityIndex);
                        invoice.setNewWaterIndex(newWaterIndex);
                        invoice.setOldWaterIndex(oldWaterIndex);

                        Double electricityDifference = newElectricityIndex - oldElectricityIndex;
                        Double waterDifference = newWaterIndex - oldWaterIndex;

                        Double wifiBill = wifiCash.getWifiBill() != null ? wifiCash.getWifiBill() : 0.0;
                        Double total = roomCash.getRoomBill()
                                + (electricityDifference * electricityCash.getElectricityBill())
                                + (waterDifference * waterCash.getWaterBill() + wifiBill);

                        invoice.setTotalPrice(total);
                        invoice.setCreateDate(new Date());
                    } else {
                        throw new IllegalArgumentException("Tháng của chỉ số không phù hợp!");
                    }
                } else {
                    throw new IllegalArgumentException(
                            "Chỉ số điện và nước không phù hợp của " + motelRoom.getDescriptions());
                }

            } else {
                throw new IllegalArgumentException(
                        "Chỉ số điện và nước chưa được tạo cho " + motelRoom.getDescriptions());
            }

            Integer invoiceStatusId = invoiceModel.getInvoiceStatusId();
            if (invoiceStatusId == null) {
                throw new IllegalArgumentException("Không tìm thấy trạng thái!");
            } else {
                InvoiceStatus invoiceStatus = invoiceStatusRepository.findById(invoiceStatusId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Invalid invoice status Id: " + invoiceModel.getInvoiceStatusId()));
                invoice.setInvoiceStatus(invoiceStatus);
                invoice.setRenter(renter);
                invoiceRepository.save(invoice);

                return invoice;
            }

        } else {
            throw new IllegalArgumentException("Không tìm thấy danh sách thuê trọ!");
        }
    }

    @Override
    public Optional<Invoice> getById(Integer invoiceId) {
        return invoiceRepository.findById(invoiceId);
    }

    @Override
    public Invoice updateInvoice(Integer invoiceId, Invoice invoice) {
        Invoice invoiceById = invoiceRepository.findById(invoiceId).orElse(null);

        if (invoice.getInvoiceStatus().getInvoiceStatusId() == 2) {
            throw new IllegalArgumentException(
                    "Hóa đơn đã được thanh toán: " + invoice.getInvoiceStatus().getInvoiceStatusId());
        }

        InvoiceStatus invoiceStatus = invoiceStatusRepository.findById(invoice.getInvoiceStatus().getInvoiceStatusId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid invoice status Id: " + invoiceById.getInvoiceStatus().getInvoiceStatusId()));
        invoice.setInvoiceStatus(invoiceStatus);
        invoice.setRenter(invoiceById.getRenter());
        invoice.setNewElectricityIndex(invoiceById.getNewElectricityIndex());
        invoice.setOldElectricityIndex(invoiceById.getOldElectricityIndex());
        invoice.setNewWaterIndex(invoiceById.getNewWaterIndex());
        invoice.setOldWaterIndex(invoiceById.getOldWaterIndex());
        invoice.setCreateDate(invoiceById.getCreateDate());
        invoice.setTotalPrice(invoiceById.getTotalPrice());
        invoiceRepository.save(invoice);

        return invoice;
    }

    @Override
    public List<Object> getRevenueByMonth() {
        return invoiceRepository.getRevenueByMonth();
    }

    @Override
    public List<Object> getRevenueByYear() {
        return invoiceRepository.getRevenueByYear();
    }

}
