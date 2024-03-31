package com.motel.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.motel.entity.ElectricityCash;
import com.motel.entity.Indexs;
import com.motel.entity.Invoice;
import com.motel.entity.InvoiceStatus;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.entity.RoomCash;
import com.motel.entity.WaterCash;
import com.motel.model.InvoiceModel;
import com.motel.repository.ElectricityCashRepository;
import com.motel.repository.IndexsRepository;
import com.motel.repository.InvoiceRepository;
import com.motel.repository.InvoiceStatusRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.RenterRepository;
import com.motel.repository.RoomCashRepository;
import com.motel.repository.WaterCashRepository;
import com.motel.service.InvoiceService;

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

    @GetMapping("/admin/invoice")
    public String getFormInvoice(Model model) {
        model.addAttribute("invoices", invoiceService.getAll());
        return "/admin/invoice/invoice-list";
    }

    @GetMapping("/admin/invoice/add-invoice")
    public String getFormAddInvoice(@ModelAttribute("invoice") InvoiceModel invoiceModel, Model model) {
        List<Renter> renters = renterRepository.findAll();
        model.addAttribute("renters", renters);

        List<InvoiceStatus> invoiceStatus = invoiceStatusRepository.findAll();
        model.addAttribute("invoiceStatues", invoiceStatus);
        return "/admin/invoice/add-invoice";
    }

    @PostMapping("/admin/invoice/add-invoice")
    public String addInvoice(@ModelAttribute("invoice") InvoiceModel invoiceModel, Model model) {

        List<Renter> renters = renterRepository.findAll();
        model.addAttribute("renters", renters);

        List<InvoiceStatus> invoiceStatues = invoiceStatusRepository.findAll();
        model.addAttribute("invoiceStatues", invoiceStatues);

        try {
            invoiceService.addInvoice(invoiceModel);
            model.addAttribute("success", true);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "/admin/invoice/add-invoice";
        }
        return "redirect:/admin/invoice";
    }

    @GetMapping("/admin/invoice/update-invoice/{invoiceId}")
    public String getFormUpdateInvoice(@PathVariable("invoiceId") Integer invoiceId, Model model,
            @ModelAttribute("invoice") Invoice invoice) {
        Invoice invoices = invoiceRepository.getById(invoiceId);
        model.addAttribute("invoices", invoices);

        System.out.println(invoices.getTotalPrice() + " : " + invoices.getCreateDate());

        List<Renter> renters = renterRepository.findAll();
        model.addAttribute("renters", renters);

        List<InvoiceStatus> invoiceStatuses = invoiceStatusRepository.findAll();
        model.addAttribute("invoiceStatuses", invoiceStatuses);

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
    //         @RequestParam(value = "vnp_OrderInfo") String orderInfo,
    //         @RequestParam(value = "vnp_ResponseCode") String responseCode) {
    //     String invoiceIdString = orderInfo.substring(orderInfo.length() - 2);

    //     if (responseCode.equals("00")) {
    //         Invoice invoice = invoiceRepository.findById(Integer.parseInt(invoiceIdString))
    //                 .orElseThrow(() -> new IllegalArgumentException("Không tồn tại hợp đồng này của sinh viên"));
    //         invoice.setInvoiceStatus(invoiceStatusRepository.findAll().get(0));
    //         invoiceRepository.save(invoice);
    //     }
    //     return "redirect:/admin/invoice";
    // }

}
