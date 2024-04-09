package com.motel.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.Motel;
import com.motel.entity.Post;
import com.motel.repository.FavoriteRoomRepository;
import com.motel.repository.InvoiceRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.PostRepository;
import com.motel.repository.RenterRepository;
import com.motel.service.InvoiceService;

@RestController
public class StatisticRestController {

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    MotelRoomRepository motelRoomRepository;

    @Autowired
    MotelRepository motelRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FavoriteRoomRepository favoriteRoomRepository;

    @Autowired
    RenterRepository renterRepository;

    @GetMapping("/api/revenue-by-month")
    public ResponseEntity<?> getRevenueByMonth() {
        List<Object> revenueByMonthList = invoiceService.getRevenueByMonth();
        return ResponseEntity.ok(revenueByMonthList);
    }

    @GetMapping("/api/revenue-by-year")
    public ResponseEntity<?> getRevenueByYear() {
        List<Object> revenueByMonthList = invoiceService.getRevenueByYear();
        return ResponseEntity.ok(revenueByMonthList);
    }

    @GetMapping("/api/month")
    public ResponseEntity<?> getMonth(@RequestParam int year) {
        List<Object> revenueByMonthList = invoiceRepository.getMonth(year);
        return ResponseEntity.ok(revenueByMonthList);
    }

    @GetMapping("/api/year")
    public ResponseEntity<?> getRevenueByYear(@RequestParam int year) {
        List<Object> revenueByYear = invoiceRepository.findByYear(year);
        return ResponseEntity.ok(revenueByYear);
    }

    @GetMapping("/api/statistic-renter")
    public ResponseEntity<Object> getStatisticRenter() {
        List<Object> statisticRenter = motelRoomRepository.statisticRenter();
        return ResponseEntity.ok(statisticRenter);
    }

    @GetMapping("/api/statistic-motel")
    public ResponseEntity<List<Motel>> getStatisticMotels(@RequestParam int motelRoomId) {
        List<Motel> statisticMotels = motelRepository.statisticMotels(motelRoomId);
        return ResponseEntity.ok(statisticMotels);
    }

    @GetMapping("/api/motels")
    public ResponseEntity<List<Motel>> getMotels() {
        List<Motel> motels = motelRepository.findAll();
        return ResponseEntity.ok(motels);
    }

    @GetMapping("/api/motels-renter")
    public ResponseEntity<List<Motel>> getMotelsRenter() {
        List<Motel> motelsRenter = motelRepository.statisticMotelsNotInRenter();
        return ResponseEntity.ok(motelsRenter);
    }

    @GetMapping("/api/post-to-day")
    public ResponseEntity<Object> getPostToDay() {
        Object post = postRepository.findPostToDay();
        return ResponseEntity.ok(post);
    }

    @GetMapping("/api/favorite-to-day")
    public ResponseEntity<Object> getFavoriteToDay() {
        Object faObject = favoriteRoomRepository.findFavoriteToDay();
        return ResponseEntity.ok(faObject);
    }

    @GetMapping("/api/favorite-to-day-motel-room")
    public ResponseEntity<Object> getFavoriteToDayMotelRoom() {
        Object faObject = favoriteRoomRepository.findFavoriteToDayMotelRoom();
        return ResponseEntity.ok(faObject);
    }

    @GetMapping("/api/post-to-day-motel-room")
    public ResponseEntity<Object> getPostToDayMotelRoom() {
        Object poObject = postRepository.findPostToDayMotelRoom();
        return ResponseEntity.ok(poObject);
    }

    @GetMapping("/api/count-renter")
    public ResponseEntity<Object> getCountRenter() {
        Object reObject = renterRepository.statisticCountRenter();
        return ResponseEntity.ok(reObject);
    }

    @GetMapping("/api/motel-renter")
    public ResponseEntity<Object> getMotelRoomRenter(@RequestParam("accountId") Integer accountId) {
        Object moObject = motelRepository.statisticMotelsRenter(accountId);
        return ResponseEntity.ok(moObject);
    }

    @GetMapping("/api/motel-renters")
    public ResponseEntity<Object> getMotelRoomRenters() {
        Object moObject = motelRepository.statisticMotelsRenters();
        return ResponseEntity.ok(moObject);
    }

    @GetMapping("/api/invoice-status")
    public ResponseEntity<Object> getInvoiceStatus() {
        Object inObject = invoiceRepository.getInvoiceStatus();
        return ResponseEntity.ok(inObject);
    }

    @GetMapping("/api/invoice-status-detail")
    public ResponseEntity<Object> getInvoiceStatusDetail() {
        Object inObject = invoiceRepository.getInvoiceStatusDetail();
        return ResponseEntity.ok(inObject);
    }
}
