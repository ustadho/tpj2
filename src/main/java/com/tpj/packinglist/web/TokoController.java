/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.web;

import com.tpj.packinglist.dao.MerkDao;
import com.tpj.packinglist.dao.TokoDao;
import com.tpj.packinglist.dao.jdbc.LookupDao;
import com.tpj.packinglist.domain.Merk;
import com.tpj.packinglist.domain.Toko;
import java.security.InvalidParameterException;
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
@RequestMapping("/api/master/toko")
public class TokoController {

    @Autowired
    TokoDao dao;

    @Autowired
    MerkDao merkDao;

    @Autowired
    LookupDao lookupDao;
    
//    @Autowired
//    AppService appService;
    private final Logger logger = LoggerFactory.getLogger(TokoController.class);

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<Toko> cariSemua() {
        Iterable<Toko> hasil=dao.findAll();
        for(Toko x: hasil){
            fixLie(x);
        }
        return hasil;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Toko> saringSemua(
            @RequestParam(required = false) String search,
            Pageable pageable,
            HttpServletResponse respons) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "nama");
        Page<Toko> result = dao.findAll(pr);
        for(Toko x: result){
            fixLie(x);
        }
        return result;
    }

    @RequestMapping(value = "{column}/{value}", method = RequestMethod.GET)
    public Toko cariSatu(@PathVariable("column") String column, @PathVariable("value") String value) {
        Toko hasil=null;
        if (column.equalsIgnoreCase("id")) {
            hasil = dao.findOne(Integer.valueOf(value));
        } else if (column.equalsIgnoreCase("nama")) {
            hasil = dao.findByNama(value);
        } else {
            throw new InvalidParameterException("column '" + column + "' not available");
        }
        fixLie(hasil);
        return hasil;
    }

    @RequestMapping(value = "/{nama}", method = RequestMethod.GET)
    @ResponseBody
    public Page<Toko> cariBerdasarkanNama(@PathVariable("nama") String nama,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "nama");
        Page<Toko> hasil= dao.filter("%" + nama.toUpperCase() + "%", pr);
        for(Toko t: hasil){
            fixLie(t);
        }
        return hasil;
    }
    
    @RequestMapping(value = "/idkota/cari/{idkota}/{cari:.+}", method = RequestMethod.GET)
    @ResponseBody
    public Object cariComposite(@PathVariable("idkota") String idkota, @PathVariable("cari") String cari,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "tanggal");
        return lookupDao.lookupToko((cari.equals("null") ? "" : "%" + cari.toUpperCase() + "%"), idkota, pr);
    }


    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void hapus(@PathVariable String id) {
        Toko x = dao.findOne(Integer.valueOf(id));
        if (x == null) {
            throw new InvalidParameterException("Cabang '" + id + "' tidak ditemukan!");
        }
        dao.delete(x);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Toko simpan(@RequestBody Toko x) {
//        User u = appService.getCurrentUser();
        if (x.getListMerk() != null) {
            for (Merk m : x.getListMerk()) {
                m.setToko(x);
            }
        }
        return dao.save(x);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Toko perbarui(@PathVariable String id, @RequestBody Toko x) {
        Toko r = dao.findOne(Integer.valueOf(id));
        if (r == null) {
            throw new InvalidParameterException("Toko dengan nama '" + x.getNama() + "' tidak ditemukan!");
        }
        if (x.getListMerk() != null) {
            for (Merk m : x.getListMerk()) {
                m.setToko(x);
            }
        }
        x.setId(r.getId());
        return dao.save(x);
    }

    
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long countAll() {
        return dao.count();
    }
    
    @RequestMapping(value = "toko/lookup/{s}", method = RequestMethod.GET)
    public Object lookupToko(@PathVariable("s") String s) {
        return lookupDao.lookupToko(s);
    }
    
    @RequestMapping(value = "toko/list/kapal-berangkat/{id}", method = RequestMethod.GET)
    public Object listPerKota(@PathVariable("id") Integer id) {
        return lookupDao.lookupTokoPerKapalBerangkat(id);
    }
    
    @RequestMapping(value = "merk/list/kapal-berangkat/{id}", method = RequestMethod.GET)
    public Object listMerkPerKota(@PathVariable("id") Integer id) {
        return lookupDao.lookupMerkTokoPerKapalBerangkat(id);
    }
    
    @RequestMapping(value = "merk/lookup/{ik}/{s}", method = RequestMethod.GET)
    public Object lookupMerk(@PathVariable("ik") Integer ik, @PathVariable("s") String s) {
        return lookupDao.lookupMerk(s, ik);
    }
    
    @RequestMapping(value = "merk/lookup/{ik}", method = RequestMethod.GET)
    public Object lookupMerkAll(@PathVariable("ik") Integer ik) {
        return lookupDao.lookupMerk("", ik);
    }
    
    @RequestMapping(value = "merk/findOne/{id}", method = RequestMethod.GET)
    public Object findOneMerk(@PathVariable("id") Integer id) {
        return merkDao.findOne(id);
    }
    
    public void fixLie(Toko t){
        if(t==null)
            return;
        for(Merk m: t.getListMerk()){
            m.setToko(null);
        }
    }
}
