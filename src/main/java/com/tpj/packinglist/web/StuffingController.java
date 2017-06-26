/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.web;

import com.tpj.packinglist.dao.StuffingDao;
import com.tpj.packinglist.dao.jdbc.LookupDao;
import com.tpj.packinglist.domain.Stuffing;
import java.security.InvalidParameterException;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * @author ustadho
 */
@CrossOrigin
@RestController
@RequestMapping("api/transaksi/stuffing")
public class StuffingController {
    @Autowired
    StuffingDao dao;
    
    @Autowired
    LookupDao lookupDao;
    private final Logger logger = LoggerFactory.getLogger(StuffingController.class);
    
    @ResponseBody
    @RequestMapping(value = "open/lookup/{ik}")
    public Object lookupStuffingOpen(@PathVariable("ik") Integer idKota){
        return lookupDao.lookupStuffingOpen(idKota);
    }
    @ResponseBody
    @RequestMapping(value = "aktif/lookup/{ik}")
    public Object lookupStuffingAktif(@PathVariable("ik") Integer idKota){
        return lookupDao.lookupStuffingAktif(idKota);
    }
    

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<Stuffing> cariSemua() {
        return dao.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Stuffing> saringSemua(
            @RequestParam(required = false) String search,
            Pageable pageable,
            HttpServletResponse respons) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "kontainer.nomor");
        Page<Stuffing> result = dao.findAll(pr);
        return result;
    }

    @RequestMapping(value = "{column}/{value}", method = RequestMethod.GET)
    public Stuffing cariSatu(@PathVariable String column, @PathVariable String value) {
        if (column.equalsIgnoreCase("kode")) {
            return dao.findOne(Integer.valueOf(value));
        } else {
            throw new InvalidParameterException("column '" + column + "' not available");
        }
    }

    @RequestMapping(value = "/{s}", method = RequestMethod.GET)
    @ResponseBody
    public Page<Stuffing> cariBerdasarkanNama(@PathVariable("s") String s,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "kontainer.nomor");
        return dao.filter("%" + s.toUpperCase() + "%", pr);
    }
    
    @RequestMapping(value = "/tglawal/tglakhir/cari/{tglawal}/{tglakhir}/{cari:.+}", method = RequestMethod.GET)
    @ResponseBody
    public Object cariComposite(@PathVariable("tglawal") String tglawal, @PathVariable("tglakhir") String tglakhir, @PathVariable("cari") String cari,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "tanggal");
        return lookupDao.lookupStuffing((cari.equals("null") ? "" : "%" + cari.toUpperCase() + "%"), tglawal, tglakhir, pr);
    }
    
    @RequestMapping(value = "stuffing/list/kapal-berangkat/{id}", method = RequestMethod.GET)
    public Object listStuffingPerKapalBerangkat(@PathVariable("id") Integer id) {
        return lookupDao.lookupStuffingPerKapalBerangkat(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void hapus(@PathVariable String id) {
        Stuffing x = dao.findOne(Integer.valueOf(id));
        if (x == null) {
            throw new InvalidParameterException("Stuffing '" + id + "' tidak ditemukan!");
        }
        dao.delete(x);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void simpan(@RequestBody Stuffing x) {
//        User u = appService.getCurrentUser();
        dao.save(x);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void perbarui(@PathVariable String id, @RequestBody Stuffing x) {
        Stuffing r = dao.findOne(Integer.valueOf(id));
        if (r == null) {
            throw new InvalidParameterException("Stuffing dengan ID '" + x.getId()+ "' tidak ditemukan!");
        }
        x.setId(r.getId());
        dao.save(x);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long countAll() {
        return dao.count();
    }

}
