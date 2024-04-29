package com.motel.restcontroller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.Indexs;
import com.motel.entity.Invoice;
import com.motel.model.IndexsModel;
import com.motel.model.InvoiceModel;
import com.motel.repository.IndexsRepository;
import com.motel.service.IndexsService;

@RestController
public class IndexsRestController {

    @Autowired
    IndexsService indexsService;

    @Autowired
    IndexsRepository indexsRepository;

    @GetMapping("/api/indexs-list/{motelRoomId}")
    public ResponseEntity<List<Indexs>> listIndexs(@PathVariable("motelRoomId") Integer motelRoomId) {
        return ResponseEntity.ok(indexsService.findIndexsByMotelRoom(motelRoomId));
    }

    @GetMapping("/api/indexs-list-index/{indexId}")
    public ResponseEntity<Optional<Indexs>> listIndexsByIndex(@PathVariable("indexId") Integer indexId) {
        return ResponseEntity.ok(indexsService.getById(indexId));
    }

    @GetMapping("/api/indexs-by-year")
    public ResponseEntity<?> getIndexsByYear() {
        List<Object> listIndexsByYear = indexsRepository.getIndexsByYear();
        return ResponseEntity.ok(listIndexsByYear);
    }

    @GetMapping("/api/indexs-list-motel-room/{year}/{motelRoomId}")
    public ResponseEntity<List<Indexs>> listIndexsByYearAndMotelRoom(@PathVariable("year") int year,
            @PathVariable("motelRoomId") Integer motelRoomId) {
        return ResponseEntity.ok(indexsRepository.getIndexsByYearAndMotelRoom(year, motelRoomId));
    }

    @PutMapping("/api/update-indexs/{indexsId}")
    public ResponseEntity<Indexs> updateIndexs(@PathVariable("indexsId") Integer indexId, @RequestBody Indexs indexs,
            HttpServletRequest request) {
        if (!request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        return ResponseEntity.ok(indexsService.updateIndexs(indexId, indexs));
    }

}
