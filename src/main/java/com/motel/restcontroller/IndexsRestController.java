package com.motel.restcontroller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.Indexs;
import com.motel.entity.Invoice;
import com.motel.service.IndexsService;

@RestController
public class IndexsRestController {

    @Autowired
    IndexsService indexsService;

    @GetMapping("/api/indexs-list/{indexsId}")
    public ResponseEntity<Optional<Indexs>> listInvoice(@PathVariable("indexsId") Integer indexsId) {
        return ResponseEntity.ok(indexsService.getById(indexsId));
    }

}
