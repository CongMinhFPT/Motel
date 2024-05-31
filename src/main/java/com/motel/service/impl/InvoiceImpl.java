package com.motel.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import com.motel.service.MailerService;

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

    @Autowired
    MailerService mailerService;

    @Override
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice addInvoice(InvoiceModel invoiceModel) {
        Invoice invoice = new Invoice();
        StringBuilder errorMessage = new StringBuilder();
        MotelRoom motelRoomByModel = motelRoomRepository.findById(invoiceModel.getMotelRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Không tồn tại phòng !"));
        List<Renter> renters = motelRoomByModel.getRenter();
        if (renters.size() == 0) {
            throw new IllegalArgumentException("Phòng chưa được thuê!");
        }
        Renter renterMotelRoom = renters.get(0);

        Integer renterId = renterMotelRoom.getRenterId();
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
                                .filter(inv -> inv.getRenter().getRenterId().equals(renter.getRenterId()))
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
                                .filter(inv -> inv.getRenter().getRenterId().equals(renter.getRenterId()))
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

            List<InvoiceStatus> invoiceStatuses = invoiceStatusRepository.findAll();

            invoice.setInvoiceStatus(invoiceStatuses.get(1));
            invoice.setRenter(renter);
            invoiceRepository.save(invoice);

            mailerService.add(mimeMessage -> {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
                try {
                    helper.setTo(renter.getAccount().getEmail());
                    helper.setSubject("Nhà Trọ F.E Xin Chào!");
                    // Tạo đối tượng SimpleDateFormat để định dạng ngày
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    // Tạo nội dung của hóa đơn
                    StringBuilder invoiceContent = new StringBuilder();
                    invoiceContent.append("<html><body>");
                    invoiceContent.append("<table style='border-collapse: collapse; width: 50%; margin: auto;'>");
                    invoiceContent.append(
                            "<tr><td colspan='2' style='text-align: left;'><h2>HÓA ĐƠN THANH TOÁN</h2></td></tr>");
                    invoiceContent.append("<tr><td style='text-align: left;'><strong>Phòng:</strong></td><td>")
                            .append(renter.getMotelRoom().getDescriptions()).append("</td></tr>");
                    invoiceContent.append("<tr><td style='text-align: left;'><strong>Ngày tạo:</strong></td><td>")
                            .append(dateFormat.format(invoice.getCreateDate())).append("</td></tr>");
                    invoiceContent.append("<tr><td style='text-align: left;'><strong>Chỉ số điện cũ:</strong></td><td>")
                            .append(formatIndex(invoice.getOldElectricityIndex())).append(" kwh</td></tr>");
                    invoiceContent
                            .append("<tr><td style='text-align: left;'><strong>Chỉ số điện mới:</strong></td><td>")
                            .append(formatIndex(invoice.getNewElectricityIndex())).append(" kwh</td></tr>");
                    invoiceContent
                            .append("<tr><td style='text-align: left;'><strong>Chỉ số chênh lệch điện:</strong></td><td>")
                            .append(formatIndex(invoice.getNewElectricityIndex() - invoice.getOldElectricityIndex()))
                            .append(" kwh</td></tr>");
                    invoiceContent.append("<tr><td style='text-align: left;'><strong>Chỉ số nước cũ:</strong></td><td>")
                            .append(formatIndex(invoice.getOldWaterIndex())).append(" m³</td></tr>");
                    invoiceContent
                            .append("<tr><td style='text-align: left;'><strong>Chỉ số nước mới:</strong></td><td>")
                            .append(formatIndex(invoice.getNewWaterIndex())).append(" m³</td></tr>");
                    invoiceContent
                            .append("<tr><td style='text-align: left;'><strong>Chỉ số nước chênh lệch:</strong></td><td>")
                            .append(formatIndex(invoice.getNewWaterIndex())).append(" m³</td></tr>");
                    invoiceContent
                            .append("<tr><td style='text-align: left;'><strong>Giá điện:</strong></td><td>")
                            .append(formatCurrency(
                                    invoice.getRenter().getMotelRoom().getElectricityCash().get(0)
                                            .getElectricityBill()))
                            .append("</td></tr>");
                    invoiceContent
                            .append("<tr><td style='text-align: left;'><strong>Giá nước:</strong></td><td>")
                            .append(formatCurrency(
                                    invoice.getRenter().getMotelRoom().getWaterCash().get(0).getWaterBill()))
                            .append("</td></tr>");
                    invoiceContent
                            .append("<tr><td style='text-align: left;'><strong>Giá phòng:</strong></td><td>")
                            .append(formatCurrency(
                                    invoice.getRenter().getMotelRoom().getRoomCash().get(0).getRoomBill()))
                            .append("</td></tr>");
                    invoiceContent
                            .append("<tr><td style='text-align: left;'><strong>Giá wifi:</strong></td><td>")
                            .append(formatCurrency(
                                    invoice.getRenter().getMotelRoom().getWifiCash().get(0).getWifiBill()))
                            .append("</td></tr>");
                    invoiceContent
                            .append("<tr><td style='text-align: left;'><strong>Trạng thái hóa đơn:</strong></td><td>")
                            .append(invoice.getInvoiceStatus().getTitle()).append("</td></tr>");
                    invoiceContent.append("<tr><td style='text-align: left;'><strong>Tổng giá tiền:</strong></td><td>")
                            .append(formatCurrency(invoice.getTotalPrice())).append("</td></tr>");
                    invoiceContent.append("</table>");
                    invoiceContent.append("</body></html>");

                    // Thiết lập nội dung hóa đơn trong email
                    helper.setText(invoiceContent.toString(), true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });

            return invoice;

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
                    "Hóa đơn đã được thanh toán: " + invoiceById.getCreateDate());
        }

        InvoiceStatus invoiceStatus = invoiceStatusRepository.findById(invoice.getInvoiceStatus().getInvoiceStatusId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid invoice status Id: " + invoiceById.getInvoiceStatus().getInvoiceStatusId()));

        if (invoiceStatus == null) {
            throw new IllegalArgumentException("Chưa chọn trạng thái!");
        }

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

    private String formatCurrency(Double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(amount).replace(",", ".") + " đ";
    }

    private String formatIndex(double index) {
        return String.format("%.0f", index);
    }

}
