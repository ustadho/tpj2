/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.web;

import com.tpj.packinglist.dao.MerkDao;
import com.tpj.packinglist.dao.BahanSjDao;
import com.tpj.packinglist.dao.jdbc.LookupDao;
import com.tpj.packinglist.domain.Merk;
import com.tpj.packinglist.domain.BahanSj;
import com.tpj.packinglist.domain.BahanSjDetail;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author faheem
 */
@CrossOrigin
@RestController
@RequestMapping("/api/transaksi/bahan-sj")
public class BahanSjController {

    @Autowired
    BahanSjDao dao;

    @Autowired
    LookupDao lookupDao;

//    @Autowired
//    AppService appService;
    private final Logger logger = LoggerFactory.getLogger(BahanSjController.class);
    private final String SESSION_KEY_LAMPIRAN = "sessionKeyPathLampiran";
    private final List<String> FILE_EXTENSION = Arrays.asList("png", "jpg", "jpeg");

    @RequestMapping(value = "/lampirancors", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin
    @ResponseBody
    public Map<String, Object> uploadCors(@RequestParam(value = "file", required = true) MultipartFile file,
            HttpServletRequest request, HttpSession session) {
        Map<String, Object> result = new HashMap();

        if (file == null) {
            System.out.println("logo.isEmpty() || logo == null");
            result.put("msg", "No logo uploaded");
            result.put("status", "400");
        } else {
            List<String> listPath = new ArrayList<>();
            String tmpPath = "";
            try {
                String extension = tokenizer(file.getOriginalFilename(), ".");
                if (FILE_EXTENSION.contains(extension.toLowerCase())) {
//                        String logoname = file[i].getOriginalFilename();
                    String logoname = UUID.randomUUID().toString();
                    byte[] bytes = file.getBytes();

                    // Creating the directory to store file
                    String rootPath = System.getProperty("catalina.home");
                    File dir = new File(rootPath + File.separator + "tmpFiles");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    // Create the file on server
                    File serverFile = new File(dir.getAbsolutePath()
                            + File.separator + logoname + "-" + file.getOriginalFilename());
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    tmpPath = serverFile.getAbsolutePath();
                    logger.info("Server File Location="
                            + serverFile.getAbsolutePath());
                    listPath.add(serverFile.getAbsolutePath());
                } else {
                    result.put("msg", "File extensions not permitted");
                    result.put("status", "400");
                }
                List<String> listPathSession = (List<String>) session.getAttribute(SESSION_KEY_LAMPIRAN);
                if (listPathSession != null) {
                    listPathSession.addAll(listPath);
                    session.setAttribute(SESSION_KEY_LAMPIRAN, listPathSession);
                } else {
                    session.setAttribute(SESSION_KEY_LAMPIRAN, listPath);
                }
                logger.warn("listPathSession size : [{}]", (listPathSession != null ? listPathSession.size() : listPath.size()));
                System.out.println("upload ok");
                result.put("tmpPath", tmpPath);
                result.put("msg", "Upload Succeded");
                result.put("status", "200");
            } catch (IOException ex) {
                System.out.println("upload error " + ex.getMessage());
                result.put("msg", ex.getMessage());
                result.put("status", "500");
            } catch (IllegalStateException ex) {
                System.out.println("upload error " + ex.getMessage());
                result.put("msg", ex.getMessage());
                result.put("status", "500");
            }
        }
        return result;
    }

    private String tokenizer(String filename, String token) {
        StringTokenizer st = new StringTokenizer(filename, token);
        String result = "";
        while (st.hasMoreTokens()) {
            result = st.nextToken();
        }
        return result;
    }

    @RequestMapping(value = "/ambil-bytea-terupload", method = RequestMethod.GET)
    public Object ambilByteaTerupload(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws FileNotFoundException, IOException {
        List<Map<String, Object>> files = new ArrayList<>();

        if (session.getAttribute(SESSION_KEY_LAMPIRAN) != null) {
            List<String> listPath = (List<String>) session.getAttribute(SESSION_KEY_LAMPIRAN);
            System.out.println("files path : ");
            for (int i = 0; i < listPath.size(); i++) {
                System.out.print(listPath.get(i) + " ");
            }
            File[] tmpLogo = new File[listPath.size()];
            for (int i = 0; i < listPath.size(); i++) {
                tmpLogo[i] = new File(listPath.get(i));
                System.out.println(listPath.get(i) + " exists " + tmpLogo[i].exists());
                FileInputStream fisLogo = new FileInputStream(tmpLogo[i]);
                Map<String, Object> hasil = new HashMap<String, Object>();
                hasil.put("fileContent", IOUtils.toByteArray(fisLogo));
                files.add(hasil);
                tmpLogo[i].delete();
            }
            session.removeAttribute(SESSION_KEY_LAMPIRAN);
        }
        return files;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<BahanSj> cariSemua() {
        Iterable<BahanSj> hasil = dao.findAll();
        return hasil;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<BahanSj> saringSemua(
            @RequestParam(required = false) String search,
            Pageable pageable,
            HttpServletResponse respons) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "nama");
        Page<BahanSj> result = dao.findAll(pr);
        return result;
    }

    @RequestMapping(value = "{column}/{value}", method = RequestMethod.GET)
    public BahanSj cariSatu(@PathVariable("column") String column, @PathVariable("value") String value) {
        BahanSj hasil = null;
        if (column.equalsIgnoreCase("kode")) {
            hasil = dao.findOne(Integer.valueOf(value));
        } else {
            throw new InvalidParameterException("column '" + column + "' not available");
        }
        return hasil;
    }

    @RequestMapping(value = "/{nama}", method = RequestMethod.GET)
    @ResponseBody
    public Page<BahanSj> cariBerdasarkanNama(@PathVariable("nama") String nama,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "id");
        Page<BahanSj> hasil = dao.filter("%" + nama.toUpperCase() + "%", pr);
        return hasil;
    }

    @RequestMapping(value = "/tglawal/tglakhir/outstand/cari/{tglawal}/{tglakhir}/{outstand}/{cari:.+}", method = RequestMethod.GET)
    @ResponseBody
    public Object cariComposite(@PathVariable("tglawal") String tglawal, @PathVariable("tglakhir") String tglakhir, @PathVariable("outstand") Boolean outstand, @PathVariable("cari") String cari,
            Pageable pageable,
            HttpServletResponse response) {
        PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.ASC, "tanggal");
        return lookupDao.lookupBahanSj((cari.equals("null") ? "" : "%" + cari.toUpperCase() + "%"), outstand, tglawal, tglakhir, pr);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void hapus(@PathVariable String id) {
        BahanSj x = dao.findOne(Integer.valueOf(id));
        if (x == null) {
            throw new InvalidParameterException("Cabang '" + id + "' tidak ditemukan!");
        }
        dao.delete(x);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void simpan(@RequestBody BahanSj x) {
//        User u = appService.getCurrentUser();
        if (x.getListBahanDetail() != null) {
            for (BahanSjDetail m : x.getListBahanDetail()) {
                m.setBahan(x);
            }
        }
        dao.save(x);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void perbarui(@PathVariable String id, @RequestBody BahanSj x) {
        BahanSj r = dao.findOne(Integer.valueOf(id));
        if (r == null) {
            throw new InvalidParameterException("BahanSj dengan Id '" + x.getId() + "' tidak ditemukan!");
        }
        if (x.getListBahanDetail() != null) {
            for (BahanSjDetail m : x.getListBahanDetail()) {
                m.setBahan(x);
            }
        }
        x.setId(r.getId());
        dao.save(x);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long countAll() {
        return dao.count();
    }

}
