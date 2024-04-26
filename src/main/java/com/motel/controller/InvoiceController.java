package com.motel.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.CustomUserDetails;
import com.motel.entity.ElectricityCash;
import com.motel.entity.Indexs;
import com.motel.entity.Invoice;
import com.motel.entity.InvoiceStatus;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.entity.RoomCash;
import com.motel.entity.WaterCash;
import com.motel.model.InvoiceModel;
import com.motel.repository.ElectricityCashRepository;
import com.motel.repository.IndexsRepository;
import com.motel.repository.InvoiceRepository;
import com.motel.repository.InvoiceStatusRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.RenterRepository;
import com.motel.repository.RoomCashRepository;
import com.motel.repository.WaterCashRepository;
import com.motel.service.InvoiceService;
import com.motel.service.impl.ManageMotelImpl;

@Controller
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

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
    InvoiceStatusRepository invoiceStatusRepository;

    @Autowired
    ManageMotelImpl manageMotelImpl;

    @Autowired
    MotelRepository motelRepository;

    @GetMapping("/admin/invoice")
    public String getFormInvoice(Model model) {
        if (manageMotelImpl.CheckLogin().isPresent()) {
            CustomUserDetails customUserDetails = manageMotelImpl.CheckLogin().get();
            if (manageMotelImpl.CheckAccountSetIdMotel(customUserDetails)) {
                Motel motel = motelRepository.getById(customUserDetails.getMotelid());

                List<MotelRoom> motelRooms = motel.getMotelRoom();
                List<Invoice> invoices = new ArrayList<>();

                for (MotelRoom motelRoom : motelRooms) {
                    for (Renter renter : motelRoom.getRenter()) {
                        for (Invoice invoice : renter.getInvoice()) {
                            invoices.add(invoice);
                        }
                    }
                }

                invoices.sort(Comparator.comparing(Invoice::getCreateDate).reversed());
                manageMotelImpl.SetModelMotel(model);
                model.addAttribute("invoices", invoices);
                return "/admin/invoice/invoice-list";
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }
    }

    @GetMapping("/admin/invoice/add-invoice")
    public String getFormAddInvoice(@ModelAttribute("invoice") InvoiceModel invoiceModel, Model model) {

        if (manageMotelImpl.CheckLogin().isPresent()) {
            CustomUserDetails customUserDetails = manageMotelImpl.CheckLogin().get();
            if (manageMotelImpl.CheckAccountSetIdMotel(customUserDetails)) {
                Motel motel = motelRepository.getById(customUserDetails.getMotelid());

                List<MotelRoom> motelRooms = motel.getMotelRoom();
                List<MotelRoom> motelRoomsAdd = new ArrayList<>();

                for (MotelRoom motelRoom : motelRooms) {
                    motelRoomsAdd.add(motelRoom);
                }

                model.addAttribute("renters", motelRoomsAdd);
                manageMotelImpl.SetModelMotel(model);
                return "/admin/invoice/add-invoice";

            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }
    }

    @PostMapping("/admin/invoice/add-invoice")
    public String addInvoice(@ModelAttribute("invoice") InvoiceModel invoiceModel, Model model) {

        if (manageMotelImpl.CheckLogin().isPresent()) {
            CustomUserDetails customUserDetails = manageMotelImpl.CheckLogin().get();
            if (manageMotelImpl.CheckAccountSetIdMotel(customUserDetails)) {
                Motel motel = motelRepository.getById(customUserDetails.getMotelid());

                List<MotelRoom> motelRooms = motel.getMotelRoom();
                List<MotelRoom> motelRoomsAdd = new ArrayList<>();

                for (MotelRoom motelRoom : motelRooms) {
                    motelRoomsAdd.add(motelRoom);
                }

                model.addAttribute("renters", motelRoomsAdd);

                try {
                    invoiceService.addInvoice(invoiceModel);
                    model.addAttribute("success", true);
                } catch (IllegalArgumentException e) {
                    model.addAttribute("error", e.getMessage());
                    return "/admin/invoice/add-invoice";
                }
                return "redirect:/admin/invoice";

            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }

    }

    @GetMapping("/admin/invoice/update-invoice/{invoiceId}")
    public String getFormUpdateInvoice(@PathVariable("invoiceId") Integer invoiceId, Model model,
            @ModelAttribute("invoice") Invoice invoice) {
        Invoice invoices = invoiceRepository.getById(invoiceId);
        invoice.setInvoiceStatus(invoices.getInvoiceStatus());
        model.addAttribute("invoices", invoices);
        // model.addAttribute("invoiceStatus",
        // invoices.getInvoiceStatus().getInvoiceStatusId());

        List<Renter> renters = renterRepository.findAll();
        model.addAttribute("renters", renters);

        List<InvoiceStatus> invoiceStatuses = invoiceStatusRepository.findAll();
        model.addAttribute("invoiceStatuses", invoiceStatuses);
        manageMotelImpl.SetModelMotel(model);
        return "/admin/invoice/update-invoice";
    }

    @PostMapping("/admin/invoice/update-invoice/{invoiceId}")
    public String updateInvoice(@PathVariable("invoiceId") Integer invoiceId,
            @ModelAttribute("invoice") Invoice invoice, Model model) {

        Invoice invoices = invoiceRepository.getById(invoiceId);
        model.addAttribute("invoices", invoices);

        List<Renter> renters = renterRepository.findAll();
        model.addAttribute("renters", renters);

        List<InvoiceStatus> invoiceStatuses = invoiceStatusRepository.findAll();
        model.addAttribute("invoiceStatuses", invoiceStatuses);
        manageMotelImpl.SetModelMotel(model);
        try {
            invoiceService.updateInvoice(invoiceId, invoice);
            model.addAttribute("success", true);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "/admin/invoice/update-invoice";
        }
        return "/admin/invoice/update-invoice";
    }

    // @GetMapping("/payment_infor")
    // public String transaction(
    // @RequestParam(value = "vnp_OrderInfo") String orderInfo,
    // @RequestParam(value = "vnp_ResponseCode") String responseCode) {
    // String invoiceIdString = orderInfo.substring(orderInfo.length() - 2);

    // if (responseCode.equals("00")) {
    // Invoice invoice =
    // invoiceRepository.findById(Integer.parseInt(invoiceIdString))
    // .orElseThrow(() -> new IllegalArgumentException("Không tồn tại hợp đồng này
    // của sinh viên"));
    // invoice.setInvoiceStatus(invoiceStatusRepository.findAll().get(0));
    // invoiceRepository.save(invoice);
    // }
    // return "redirect:/admin/invoice";
    // }

}
