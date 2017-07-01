/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.web;

import com.tpj.packinglist.dao.jdbc.ReportDao;
import java.text.ParseException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ustadho
 */
@Controller
@RequestMapping("api/report/")
public class ReportController {

    @Autowired
    ServletContext context;

    @Autowired
    ReportDao dao;

    private final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @RequestMapping(value = "coba*", method = RequestMethod.GET)
    private ModelMap chartBruto(HttpServletRequest request) throws ParseException {
        String uri = request.getRequestURI();
        String format = uri.substring(uri.lastIndexOf(".") + 1);

        String tgl = request.getParameter("t");
        String realPath = context.getRealPath("/WEB-INF/templates/jrxml/") + System.getProperty("file.separator");
        realPath = realPath.replace("\\", "\\\\");
        logger.warn("format: [{}]", format);
        logger.warn("tgl: [{}]", tgl);

        return new ModelMap()
                //                .addAttribute("tanggal1", tg1)
                //                .addAttribute("logo", realPath + "igg-kop.jpg")
                .addAttribute("format", format)
                .addAttribute("dataSource", null);
    }

    @RequestMapping(value = "per-stuffing*", method = RequestMethod.GET)
    private ModelMap perStufing(HttpServletRequest request) throws ParseException {
        String uri = request.getRequestURI();
        String format = uri.substring(uri.lastIndexOf(".") + 1);

        String id = request.getParameter("id");
        Boolean ex = Boolean.valueOf(request.getParameter("ex"));
        String realPath = context.getRealPath("/WEB-INF/templates/jrxml/") + System.getProperty("file.separator");
        realPath = realPath.replace("\\", "\\\\");
        logger.warn("format: [{}]", format);
        logger.warn("id: [{}]", id);

        return new ModelMap()
                //                .addAttribute("tanggal1", tg1)
                .addAttribute("realPath", realPath)
                .addAttribute("lapExpedisi", ex)
                .addAttribute("format", format)
                .addAttribute("dataSource", dao.perStuffing(Integer.valueOf(id)));
    }

    @RequestMapping(value = "per-toko*", method = RequestMethod.GET)
    private ModelMap perToko(HttpServletRequest request) throws ParseException {
        String uri = request.getRequestURI();
        String format = uri.substring(uri.lastIndexOf(".") + 1);

        String id = request.getParameter("id");
        String it = request.getParameter("it");
        String realPath = context.getRealPath("/WEB-INF/templates/jrxml/") + System.getProperty("file.separator");
        realPath = realPath.replace("\\", "\\\\");
        logger.warn("format: [{}]", format);
        logger.warn("id: [{}]", id);
        logger.warn("idToko: [{}]", it);

        return new ModelMap()
                //                .addAttribute("tanggal1", tg1)
                //                .addAttribute("logo", realPath + "igg-kop.jpg")
                .addAttribute("realPath", realPath)
                .addAttribute("format", format)
                .addAttribute("dataSource", dao.perKapalToko(Integer.valueOf(id), Integer.valueOf(it)));
    }

    @RequestMapping(value = "per-merk-toko*", method = RequestMethod.GET)
    private ModelMap perMerkToko(HttpServletRequest request) throws ParseException {
        String uri = request.getRequestURI();
        String format = uri.substring(uri.lastIndexOf(".") + 1);

        String id = request.getParameter("id");
        String it = request.getParameter("it");
        String realPath = context.getRealPath("/WEB-INF/templates/jrxml/") + System.getProperty("file.separator");
        realPath = realPath.replace("\\", "\\\\");
        logger.warn("format: [{}]", format);
        logger.warn("id: [{}]", id);
        logger.warn("idMerk: [{}]", it);

        return new ModelMap()
                //                .addAttribute("tanggal1", tg1)
                //                .addAttribute("logo", realPath + "igg-kop.jpg")
                .addAttribute("realPath", realPath)
                .addAttribute("format", format)
                .addAttribute("dataSource", dao.perKapalMerkToko(Integer.valueOf(id), it));
    }

    @RequestMapping(value = "jml-container-pertujuan*", method = RequestMethod.GET)
    private ModelMap jmlContainerPerTujuan(HttpServletRequest request) throws ParseException {
        String uri = request.getRequestURI();
        String format = uri.substring(uri.lastIndexOf(".") + 1);

        String tahun = request.getParameter("tahun");
        String bulan = request.getParameter("bulan");
        String realPath = context.getRealPath("/WEB-INF/templates/jrxml/") + System.getProperty("file.separator");
        realPath = realPath.replace("\\", "\\\\");
        logger.warn("format: [{}]", format);
        logger.warn("tahun: [{}]", tahun);
        logger.warn("bulan: [{}]", bulan);

        return new ModelMap()
                //                .addAttribute("tanggal1", tg1)
                //                .addAttribute("logo", realPath + "igg-kop.jpg")
                .addAttribute("realPath", realPath)
                .addAttribute("format", format)
                .addAttribute("dataSource", dao.jmlContainerPerTujuan(tahun, bulan));
    }

    @RequestMapping(value = "/rekap-coli-kubikasi", method = RequestMethod.GET)
    @ResponseBody
    private Object rekapColiKubikasiPerGrup(HttpServletRequest request) throws ParseException {
        String uri = request.getRequestURI();
        String format = uri.substring(uri.lastIndexOf(".") + 1);

        String grup = request.getParameter("grup");
        String tgl1 = request.getParameter("tgl1");
        String tgl2 = request.getParameter("tgl2");
        String order = request.getParameter("order");
        Integer limit = Integer.parseInt(request.getParameter("limit"));
        logger.warn("grup: [{}]", grup);
        logger.warn("tgl1: [{}]", tgl1);
        logger.warn("tgl2: [{}]", tgl2);
        logger.warn("order: [{}]", order);
        return dao.rekapColiKubikasiPerGrup(grup, tgl1, tgl2, order, limit);
    }
    
    @RequestMapping(value = "/list-bast/{id}", method = RequestMethod.GET)
    @ResponseBody
    private Object listBast(@PathVariable Integer id) {
        return dao.listBast(id);
    }
    
    @RequestMapping(value = "bast*", method = RequestMethod.GET)
    private ModelMap bast(HttpServletRequest request) throws ParseException {
        String uri = request.getRequestURI();
        String format = uri.substring(uri.lastIndexOf(".") + 1);

        String idKapalBerangkat = request.getParameter("k");
        String idMerk = request.getParameter("m");
        String idEmkl = request.getParameter("e");
        String realPath = context.getRealPath("/WEB-INF/templates/jrxml/") + System.getProperty("file.separator");
        realPath = realPath.replace("\\", "\\\\");
        logger.warn("format: [{}]", format);
        logger.warn("id: [{}]", idKapalBerangkat);
        logger.warn("idMerk: [{}]", idMerk);

        return new ModelMap()
                //                .addAttribute("tanggal1", tg1)
                //                .addAttribute("logo", realPath + "igg-kop.jpg")
                .addAttribute("realPath", realPath)
                .addAttribute("format", format)
                .addAttribute("dataSource", dao.bast(
                        Integer.valueOf(idKapalBerangkat), 
                        Integer.valueOf(idMerk),
                        Integer.valueOf(idEmkl)));
    }
    @RequestMapping(value = "rekap-merk*", method = RequestMethod.GET)
    private ModelMap rekapPerMerk(HttpServletRequest request) throws ParseException {
        String uri = request.getRequestURI();
        String format = uri.substring(uri.lastIndexOf(".") + 1);

        String idStuffing = request.getParameter("id");
        String realPath = context.getRealPath("/WEB-INF/templates/jrxml/") + System.getProperty("file.separator");
        realPath = realPath.replace("\\", "\\\\");
        logger.warn("format: [{}]", format);
        logger.warn("id: [{}]", idStuffing);

        return new ModelMap()
                //                .addAttribute("tanggal1", tg1)
                //                .addAttribute("logo", realPath + "igg-kop.jpg")
                .addAttribute("realPath", realPath)
                .addAttribute("format", format)
                .addAttribute("dataSource", dao.rekapMerk(Integer.valueOf(idStuffing)));
    }
}
