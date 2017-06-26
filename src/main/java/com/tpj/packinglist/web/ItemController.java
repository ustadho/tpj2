/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.web;

import com.tpj.packinglist.dao.ItemDao;
import com.tpj.packinglist.domain.Item;
import java.security.InvalidParameterException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author faheem
 */
@CrossOrigin
@RestController
@RequestMapping("/api/master/item")
public class ItemController {

    @Autowired
    ItemDao dao;

//    @Autowired
//    AppService appService;

    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<Item> cariSemua() {
        return dao.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Item> saringSemua(
            @RequestParam(required = false) String search,
            Pageable pageable,
            HttpServletResponse respons) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "nama");
        Page<Item> result = dao.findAll(pr);
        return result;
    }

    @RequestMapping(value = "cek", method = RequestMethod.GET)
    public Item cariSatu(HttpServletRequest request) {
        String kolom = request.getParameter("col");
        String nilai = request.getParameter("val");
        if (kolom.equalsIgnoreCase("kode")) {
            return dao.findOne(Integer.valueOf(nilai));        
        } else if (kolom.equalsIgnoreCase("nama")) {
            return dao.findByNama(nilai);        
        } else {
            throw new InvalidParameterException("column '" + kolom + "' not available");
        }
    }

    @RequestMapping(value = "/{nama:.+}", method = RequestMethod.GET)
    @ResponseBody
    public Page<Item> cariBerdasarkanNama(@PathVariable("nama") String nama,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "nama");
        return dao.filter("%" + nama.toUpperCase() + "%", pr);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void hapus(@PathVariable String id) {
        Item x = dao.findOne(Integer.valueOf(id));
        if (x == null) {
            throw new InvalidParameterException("Cabang '" + id + "' tidak ditemukan!");
        }
        dao.delete(x);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Item simpan(@RequestBody Item x) {
//        User u = appService.getCurrentUser();
        return dao.save(x);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void perbarui(@PathVariable String id, @RequestBody Item x) {
        Item r = dao.findOne(Integer.valueOf(id));
        if (r == null) {
            throw new InvalidParameterException("Item dengan nama '" + x.getNama() + "' tidak ditemukan!");
        }
        x.setId(r.getId());
        dao.save(x);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long countAll() {
        return dao.count();
    }

}
