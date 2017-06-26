/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.web;

import com.tpj.packinglist.dao.ItemUkuranDao;
import com.tpj.packinglist.domain.ItemUkuran;
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
@RequestMapping("/api/master/item-ukuran")
public class ItemUkuranController {
    
    @Autowired
    ItemUkuranDao dao;

//    @Autowired
//    AppService appService;
    private final Logger logger = LoggerFactory.getLogger(ItemUkuranController.class);
    
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<ItemUkuran> cariSemua() {
        return dao.findAll();
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public Page<ItemUkuran> saringSemua(
            @RequestParam(required = false) String search,
            Pageable pageable,
            HttpServletResponse respons) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "nama");
        Page<ItemUkuran> result = dao.findAll(pr);
        return result;
    }
    
    @RequestMapping(value = "cek", method = RequestMethod.GET)
    public ItemUkuran cariSatu(HttpServletRequest request) {
        String kolom = request.getParameter("col");
        String nilai = request.getParameter("val");
        if (kolom.equalsIgnoreCase("kode")) {
            return dao.findOne(Integer.valueOf(nilai));            
        } else {
            throw new InvalidParameterException("column '" + kolom + "' not available");
        }
    }
    
    @RequestMapping(value = "/filter-by-item/{idItem:.+}", method = RequestMethod.GET)
    @ResponseBody
    public Page<ItemUkuran> filterByIdItem(@PathVariable("idItem") String idItem,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "id");
        return dao.filterByIdItem(Integer.parseInt(idItem), pr);
    }
    
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void hapus(@PathVariable String id) {
        ItemUkuran x = dao.findOne(Integer.valueOf(id));
        if (x == null) {
            throw new InvalidParameterException("Cabang '" + id + "' tidak ditemukan!");
        }
        dao.delete(x);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ItemUkuran simpan(@RequestBody ItemUkuran x) {
//        User u = appService.getCurrentUser();
        return dao.save(x);
    }
    
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void perbarui(@PathVariable String id, @RequestBody ItemUkuran x) {
        ItemUkuran r = dao.findOne(Integer.valueOf(id));
        if (r == null) {
            throw new InvalidParameterException("ItemUkuran dengan id '" + x.getId() + "' tidak ditemukan!");
        }
        x.setId(r.getId());
        dao.save(x);
    }
    
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long countAll() {
        return dao.count();
    }
    
}
