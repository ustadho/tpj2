/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.web;

import com.tpj.packinglist.dao.SJStuffingDao;
import com.tpj.packinglist.dao.SuratJalanDao;
import com.tpj.packinglist.dao.jdbc.LookupDao;
import com.tpj.packinglist.domain.SJStuffing;
import com.tpj.packinglist.domain.SuratJalan;
import com.tpj.packinglist.domain.SuratJalanDetail;
import java.security.InvalidParameterException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
@RequestMapping("api/transaksi/sj")
public class SuratJalanController {

    @Autowired
    SuratJalanDao dao;

    @Autowired
    LookupDao lookupDao;
    @Autowired
    SJStuffingDao sjStuffingDao;
    private final Logger logger = LoggerFactory.getLogger(SuratJalanController.class);

    @RequestMapping("item/lookup")
    public Object lookupItemAll() {
        return lookupDao.lookupItem("");
    }

    @RequestMapping("item/lookup/{s:.+}")
    public Object lookupItem(@PathVariable("s") String s) {
        return lookupDao.lookupItem(s);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<SuratJalan> cariSemua() {
        return dao.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<SuratJalan> saringSemua(
            @RequestParam(required = false) String search,
            Pageable pageable,
            HttpServletResponse respons) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "tanggal");
        Page<SuratJalan> result = dao.findAll(pr);
        return result;
    }

    @RequestMapping(value = "{column}/{value}", method = RequestMethod.GET)
    public SuratJalan cariSatu(@PathVariable String column, @PathVariable String value) {
        if (column.equalsIgnoreCase("kode")) {
            return dao.findOne(Integer.valueOf(value));
        } else {
            throw new InvalidParameterException("column '" + column + "' not available");
        }
    }

    @RequestMapping(value = "/{s:.+}", method = RequestMethod.GET)
    @ResponseBody
    public Page<SuratJalan> cariBerdasarkanNama(@PathVariable("s") String s,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "tanggal");
        return dao.filter("%" + s.toUpperCase() + "%", pr);
    }

    @RequestMapping(value = "get-sj-stuffing/{idSjDetail}", method = RequestMethod.GET)
    public Object cariSJStuffingByIdSJDetail(@PathVariable String idSjDetail) {
        return sjStuffingDao.filterByIdSjDetail(Integer.valueOf(idSjDetail));
    }

    @RequestMapping(value = "post-sj-stuffings", method = RequestMethod.POST)
    public Object cariSJStuffingByIdSJDetail(@RequestBody List<SJStuffing> x) {
        return sjStuffingDao.save(x);
    }

    @RequestMapping(value = "/tglawal/tglakhir/idkota/cari/{tglawal}/{tglakhir}/{idkota}/{cari:.+}", method = RequestMethod.GET)
    @ResponseBody
    public Object cariComposite(@PathVariable("tglawal") String tglawal, @PathVariable("tglakhir") String tglakhir, @PathVariable("idkota") String idkota, @PathVariable("cari") String cari,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "tanggal");
        return lookupDao.lookupSuratJalan((cari.equals("null") ? "" : "%" + cari.toUpperCase() + "%"), idkota, tglawal, tglakhir, pr);
    }

    @RequestMapping(value = "infoitem/tglawal/tglakhir/idtoko/cari/{tglawal}/{tglakhir}/{idtoko}/{cari:.+}", method = RequestMethod.GET)
    @ResponseBody
    public Object cariInfoItemComposite(@PathVariable("tglawal") String tglawal, @PathVariable("tglakhir") String tglakhir, @PathVariable("idtoko") String idkota, @PathVariable("cari") String cari,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "tanggal");
        return lookupDao.lookupInfoItem((cari.equals("null") ? "" : "%" + cari.toUpperCase() + "%"), idkota, tglawal, tglakhir, pr);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void hapus(@PathVariable String id) {
        SuratJalan x = dao.findOne(Integer.valueOf(id));
        if (x == null) {
            throw new InvalidParameterException("SuratJalan '" + id + "' tidak ditemukan!");
        }
        dao.delete(x);
    }

    @RequestMapping(method = RequestMethod.POST)
    public SuratJalan simpan(@RequestBody @Valid SuratJalan x) {
        if (x.getListSuratJalanDetail() != null) {
            for (SuratJalanDetail c : x.getListSuratJalanDetail()) {
                c.setSuratJalan(x);
                for (SJStuffing sj : c.getSjStufings()) {
                    sj.setSjDetail(c);
                }
            }
        }
        dao.save(x);
        return x;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public SuratJalan perbarui(@PathVariable String id, @RequestBody SuratJalan x) {
        SuratJalan r = dao.findOne(Integer.valueOf(id));
        if (r == null) {
            throw new InvalidParameterException("SuratJalan dengan ID '" + x.getId() + "' tidak ditemukan!");
        }
        x.setId(r.getId());
        if (x.getListSuratJalanDetail() != null) {
            for (SuratJalanDetail d : x.getListSuratJalanDetail()) {
                d.setSuratJalan(x);
                for (SJStuffing sj : d.getSjStufings()) {
                    sj.setSjDetail(d);
                }
            }
        }
        dao.save(x);
        return x;
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long countAll() {
        return dao.count();
    }
}
