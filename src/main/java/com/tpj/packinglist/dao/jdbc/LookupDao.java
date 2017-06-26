/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao.jdbc;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.ui.ModelMap;

/**
 *
 * @author ustadho
 */
@Repository
public class LookupDao {

    @Autowired
    MapResultSet mr;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(LookupDao.class);

    public Object lookupStuffingOpen(Integer idKota) {
        String sql = "select st.id, kn.id id_kontainer, kn.nomor, kt.nama kota_tujuan, coalesce(k.nama,'') nama_kapal, k.id as id_kapal, kb.id as id_kapal_berangkat, kb.tgl_berangkat \n"
                + "from t_stuffing st \n"
                + "inner join m_kontainer kn on kn.id=st.id_kontainer\n"
                + "left join t_kapal_berangkat kb on kb.id=st.id_kapal_berangkat\n"
                + "left join m_kapal k on k.id=kb.id_kapal\n"
                + "left join m_kota kt on kt.id=kb.id_kota\n"
                + "where st.tgl_closing is null\n"
                + (idKota == 0 ? "" : "and st.id_kota=" + idKota + " \n")
                + "order by coalesce(kn.nomor,'')";

        return mr.mapList(sql);
    }

    public Object lookupStuffingAktif(Integer idKota) {
        String sql = "select st.id, kn.id id_kontainer, kn.nomor, kt.nama kota_tujuan, coalesce(k.nama,'') nama_kapal, k.id as id_kapal, kb.id as id_kapal_berangkat, kb.tgl_berangkat \n"
                + "from t_stuffing st \n"
                + "inner join m_kontainer kn on kn.id=st.id_kontainer\n"
                + "left join t_kapal_berangkat kb on kb.id=st.id_kapal_berangkat\n"
                + "left join m_kapal k on k.id=kb.id_kapal\n"
                + "left join m_kota kt on kt.id=kb.id_kota\n"
                + "where coalesce(st.aktif, true)=true \n"
                + (idKota == 0 ? "" : "and st.id_kota=" + idKota + " \n")
                + "order by coalesce(kn.nomor,'')";

        return mr.mapList(sql);
    }

    public Object lookupItem(String s) {
        String sql = "select i.id, coalesce(i.nama,'') nama, coalesce(u.p,0) as p, coalesce(u.l,0) l, coalesce(u.t,0) t \n"
                + "from m_item i \n"
                + "left join m_item_ukuran u on u.id_item=i.id\n"
                + "where aktif=true \n"
                + "and coalesce(i.nama,'') ilike '%" + s + "%' \n"
                + "order by coalesce(i.nama,''), coalesce(u.p,0), coalesce(u.l,0), coalesce(u.t,0) ";

        return mr.mapList(sql);
    }

    public Object lookupMerk(String s, Integer idkota) {
        String sql = "select m.id, m.nama merk, t.id id_toko, t.nama toko, t.alamat, k.nama nama_kota\n"
                + "from m_merk m \n"
                + "inner join m_toko t on t.id=m.id_toko\n"
                + "inner join m_kota k on k.id=t.id_kota\n"
                + "where t.id_kota=" + idkota + "\n"
                + "and coalesce(t.nama,'')||coalesce(m.nama,'') ilike '%" + s + "%' \n"
                + "order by t.nama, m.nama";

        return mr.mapList(sql);
    }

    public Object lookupToko(String s) {
        String sql = "select t.id, t.nama, t.alamat, k.nama nama_kota\n"
                + "from m_toko t \n"
                + "left join m_kota k on k.id=t.id_kota\n"
                + "where coalesce(t.nama,'')||coalesce(t.alamat,'') ilike '%" + s + "%' \n"
                //                + "and t.id not in(select id_toko from m_merk) \n"
                + "order by t.nama";

        return mr.mapList(sql);
    }

    public Object lookupSuratJalan(String s) {
        String sql = "select t.id, t.nama, t.alamat, k.nama nama_kota\n"
                + "from m_toko t \n"
                + "left join m_kota k on k.id=t.id_kota\n"
                + "where coalesce(t.nama,'')||coalesce(t.alamat,'') ilike '%" + s + "%' \n"
                //                + "and t.id not in(select id_toko from m_merk) \n"
                + "order by t.nama";

        return mr.mapList(sql);
    }

    public Object lookupTokoPerKapalBerangkat(Integer id) {
        String sql = "select distinct t.id, t.nama, t.alamat, t.kontak, t.telepon\n"
                + "from t_kapal_berangkat kb \n"
                + "inner join t_stuffing st on st.id_kapal_berangkat=kb.id\n"
                + "inner join t_sj_stuffing sj on sj.id_stuffing=st.id\n"
                + "inner join t_surat_jalan_detail d on d.id=sj.id_sj_detail\n"
                + "inner join t_surat_jalan h on h.id=d.id_surat_jalan\n"
                + "inner join m_merk m on m.id=h.id_merk\n"
                + "inner join m_toko t on t.id=m.id_toko\n"
                + "where st.id_kapal_berangkat=" + id + "\n"
                + "order by t.nama";

        return mr.mapList(sql);
    }

    public Object lookupMerkTokoPerKapalBerangkat(Integer id) {
        String sql = "select distinct t.id, t.nama, t.alamat, t.kontak, t.telepon, m.id as id_merk, m.nama as merk, false as terpilih \n"
                + "from t_kapal_berangkat kb \n"
                + "inner join t_stuffing st on st.id_kapal_berangkat=kb.id\n"
                + "inner join t_sj_stuffing sj on sj.id_stuffing=st.id\n"
                + "inner join t_surat_jalan_detail d on d.id=sj.id_sj_detail\n"
                + "inner join t_surat_jalan h on h.id=d.id_surat_jalan\n"
                + "inner join m_merk m on m.id=h.id_merk\n"
                + "inner join m_toko t on t.id=m.id_toko\n"
                + "where st.id_kapal_berangkat=" + id + "\n"
                + "order by t.nama, m.nama";

        System.out.println("sql : " + sql);
        return mr.mapList(sql);
    }

    public Object lookupStuffingPerKapalBerangkat(Integer id) {
        ModelMap mm = new ModelMap();
        System.out.println("cari: " + id);
        String query = "SELECT \n"
                + "  em.nama as nama_emkl, \n"
                + "  st.id, \n"
                + "  st.tgl_closing, \n"
                + "  st.aktif, \n"
                + "  kp.nama as nama_kapal, \n"
                + "  sk.nama as satuan_kirim, \n"
                + "  kb.tgl_berangkat, \n"
                + "  ko.nomor as nomor_kontainer, \n"
                + "  kt.nama as kota_tujuan\n"
                + "FROM \n"
                + "  public.t_stuffing st \n"
                + "  left join public.m_emkl em on st.id_emkl = em.id \n"
                + "  left join public.t_kapal_berangkat kb on st.id_kapal_berangkat = kb.id \n"
                + "  left join public.m_kontainer ko on st.id_kontainer = ko.id \n"
                + "  left join public.m_kota kt on st.id_kota = kt.id \n"
                + "  left join public.m_satuan_kirim sk on st.id_satuan_kirim = sk.id \n"
                + "  left join public.m_kapal kp on kb.id_kapal = kp.id\n"
                + "WHERE \n"
                + "  kb.id = " + id + "\n"
                + "order by coalesce(kb.tgl_berangkat,'1985-11-11'::date) desc, kt.nama \n";
        logger.warn("Query [{}]", query);
        return mr.mapList(query);
    }

    public Object lookupStuffing(String cari, String tglAwal, String tglAkhir, PageRequest page) {
        ModelMap mm = new ModelMap();
        System.out.println("Page.Size: " + page.getPageSize());
        System.out.println("Page.Offset: " + page.getOffset());
        System.out.println("cari: " + cari);
        String query = "SELECT \n"
                + "  em.nama as nama_emkl, \n"
                + "  st.id, \n"
                + "  st.tgl_closing, \n"
                + "  st.aktif, \n"
                + "  kp.nama as nama_kapal, \n"
                + "  sk.nama as satuan_kirim, \n"
                + "  kb.tgl_berangkat, \n"
                + "  ko.nomor as nomor_kontainer, \n"
                + "  kt.nama as kota_tujuan\n"
                + "FROM \n"
                + "  public.t_stuffing st \n"
                + "  left join public.m_emkl em on st.id_emkl = em.id \n"
                + "  left join public.t_kapal_berangkat kb on st.id_kapal_berangkat = kb.id \n"
                + "  left join public.m_kontainer ko on st.id_kontainer = ko.id \n"
                + "  left join public.m_kota kt on st.id_kota = kt.id \n"
                + "  left join public.m_satuan_kirim sk on st.id_satuan_kirim = sk.id \n"
                + "  left join public.m_kapal kp on kb.id_kapal = kp.id\n"
                + "WHERE \n"
                + "  coalesce(kb.tgl_berangkat,current_date) between '" + tglAwal + "'::date and '" + tglAkhir + "'::date\n"
                + "  and coalesce(em.nama,'')||coalesce(kp.nama,'')||coalesce(sk.nama,'')||coalesce(ko.nomor,'')||coalesce(kt.nama,'') ilike '%" + cari + "%' "
                + "order by coalesce(kb.tgl_berangkat,'1985-11-11'::date) desc, kt.nama \n";
        System.out.println("query all : " + query);
        Integer totalElement = mr.countRecordset(query);
        int totalPages = totalElement == 0 ? 0 : totalElement <= page.getPageSize() ? 1
                : (totalElement / page.getPageSize()) + (totalElement % page.getPageSize() >= 1 ? 1 : 0);
        boolean isFirstPage = totalPages == 0 || page.getPageNumber() == 0;
        boolean isLastPage = totalPages == 0 || page.getPageNumber() == 0;

        query = query + "limit " + page.getPageSize() + "\n"
                + "offset " + (page.getOffset());
        System.out.println("query paging : " + query);
        logger.warn("Query [{}]", query);
        mm.addAttribute("content", mr.mapList(query));
        mm.put("size", page.getPageSize());
        mm.put("totalElements", totalElement);
        mm.put("totalPages", totalPages);
        mm.put("number", page.getPageNumber());
        mm.put("firstPage", isFirstPage);
        mm.put("lastPage", isLastPage);
        return mm;
    }

    public Object lookupSuratJalan(String cari, String idKota, String tglAwal, String tglAkhir, PageRequest page) {
        ModelMap mm = new ModelMap();
        System.out.println("Page.Size: " + page.getPageSize());
        System.out.println("Page.Offset: " + page.getOffset());
        System.out.println("cari: " + cari);
        String query = "SELECT \n"
                + "  sj.id, \n"
                + "  tk.nama as pengirim, \n"
                + "  sj.tanggal, \n"
                + "  m.nama merk, \n"
                + "  tm.nama toko_tujuan, \n"
                + "  km.nama kota_tujuan, \n"
                + "  sj.nomor as no_sj, \n"
                + "  kn.nomor as no_kontainer \n"
                + "FROM \n"
                + "  public.t_surat_jalan sj \n"
                + "  left join public.m_merk m  on sj.id_merk = m.id\n"
                + "  left join public.m_toko tm on m.id_toko = tm.id   \n"
                + "  left join public.m_kota km on tm.id_kota = km.id \n"
                + "  left join public.m_toko tk on sj.id_toko = tk.id\n"
                + "  left join public.t_stuffing st on sj.id_stuffing= st.id\n"
                + "  left join public.m_kontainer kn on st.id_kontainer= kn.id\n"
                + "WHERE \n"
                + "  sj.tanggal between '" + tglAwal + "'::date and '" + tglAkhir + "'::date\n"
                + "  and case when 0 = " + idKota + " then true else km.id = " + idKota + " end\n"
                //                + "  and tk.nama||m.nama||tm.nama||km.nama||sj.nomor ilike '%" + cari + "%' order by sj.tanggal, tm.nama \n";
                + "  and coalesce(tk.nama,'')||coalesce(m.nama,'')||coalesce(tm.nama,'')||coalesce(km.nama,'')||coalesce(sj.nomor,'')||coalesce(kn.nomor,'') ilike '%" + cari + "%' order by sj.tanggal, tm.nama \n";
        System.out.println("query all : " + query);
        Integer totalElement = mr.countRecordset(query);
        int totalPages = totalElement == 0 ? 0 : totalElement <= page.getPageSize() ? 1
                : (totalElement / page.getPageSize()) + (totalElement % page.getPageSize() >= 1 ? 1 : 0);
        boolean isFirstPage = totalPages == 0 || page.getPageNumber() == 0;
        boolean isLastPage = totalPages == 0 || page.getPageNumber() == 0;

        query = query + "limit " + page.getPageSize() + "\n"
                + "offset " + (page.getOffset());
        System.out.println("query paging : " + query);
        logger.warn("Query [{}]", query);
        mm.addAttribute("content", mr.mapList(query));
        mm.put("size", page.getPageSize());
        mm.put("totalElements", totalElement);
        mm.put("totalPages", totalPages);
        mm.put("number", page.getPageNumber());
        mm.put("firstPage", isFirstPage);
        mm.put("lastPage", isLastPage);
        return mm;
    }

    public Object lookupInfoItem(String cari, String idToko, String tglAwal, String tglAkhir, PageRequest page) {
        ModelMap mm = new ModelMap();
        System.out.println("Page.Size: " + page.getPageSize());
        System.out.println("Page.Offset: " + page.getOffset());
        System.out.println("cari: " + cari);
        String query = "SELECT \n"
                + "  sj.id, \n"
                + "  sj.id_stuffing, \n"
                + "  tk.nama as pengirim, \n"
                + "  sj.tanggal, \n"
                + "  m.nama merk, \n"
                + "  i.nama as item,\n"
                + "  tm.nama toko_tujuan, \n"
                + "  km.nama kota_tujuan, \n"
                + "  sj.nomor as no_sj, \n"
                + "  kn.nomor as no_kontainer,\n"
                + "  k.nama as kapal, \n  "
                + "  sjd.coli,\n"
                + "  round(hitung_subtotal_metrik(sjd.id)::numeric,3) as kubikasi "
                + "FROM \n"
                + "  public.t_surat_jalan sj \n"
                + "  join t_surat_jalan_detail sjd on sjd.id_surat_jalan = sj.id\n"
                + "  join m_item i on i.id = sjd.id_item\n"
                + "  left join public.m_merk m  on sj.id_merk = m.id\n"
                + "  left join public.m_toko tm on m.id_toko = tm.id   \n"
                + "  left join public.m_kota km on tm.id_kota = km.id \n"
                + "  left join public.m_toko tk on sj.id_toko = tk.id\n"
                + "  left join public.t_stuffing st on sj.id_stuffing= st.id\n"
                + "  left join public.m_kontainer kn on st.id_kontainer= kn.id\n"
                + "  left join t_kapal_berangkat kb on st.id_kapal_berangkat = kb.id\n"
                + "  left join m_kapal k on k.id = kb.id_kapal\n"
                + "WHERE \n"
                + "  sj.tanggal between '" + tglAwal + "'::date and '" + tglAkhir + "'::date\n"
                + "  and case when " + idToko + " = 0 then true else tm.id = " + idToko + " end  \n"
                + "  and coalesce(tk.nama,'')||coalesce(m.nama,'')||coalesce(tm.nama,'')||coalesce(km.nama,'')||coalesce(sj.nomor,'')||coalesce(kn.nomor,'')||coalesce(i.nama,'') ilike '%" + cari + "%' order by sj.tanggal, tm.nama \n";
        System.out.println("query all : " + query);
        Integer totalElement = mr.countRecordset(query);
        int totalPages = totalElement == 0 ? 0 : totalElement <= page.getPageSize() ? 1
                : (totalElement / page.getPageSize()) + (totalElement % page.getPageSize() >= 1 ? 1 : 0);
        boolean isFirstPage = totalPages == 0 || page.getPageNumber() == 0;
        boolean isLastPage = totalPages == 0 || page.getPageNumber() == 0;

        query = query + "limit " + page.getPageSize() + "\n"
                + "offset " + (page.getOffset());
        System.out.println("query paging : " + query);
        logger.warn("Query [{}]", query);
        mm.addAttribute("content", mr.mapList(query));
        mm.put("size", page.getPageSize());
        mm.put("totalElements", totalElement);
        mm.put("totalPages", totalPages);
        mm.put("number", page.getPageNumber());
        mm.put("firstPage", isFirstPage);
        mm.put("lastPage", isLastPage);
        return mm;
    }

    public Object lookupKapalBerangkat(String cari, String idKota, String idKapal, String tglAwal, String tglAkhir, PageRequest page) {
        ModelMap mm = new ModelMap();
        System.out.println("Page.Size: " + page.getPageSize());
        System.out.println("Page.Offset: " + page.getOffset());
        System.out.println("cari: " + cari);
        String query = "SELECT \n"
                + "  kt.nama as kota, \n"
                + "  kp.nama as kapal, \n"
                + "  kb.tgl_berangkat, \n"
                + "  kb.aktif,\n"
                + "  kb.id\n"
                + "FROM \n"
                + "  public.t_kapal_berangkat kb\n"
                + "  left join public.m_kapal kp on kb.id_kapal = kp.id\n"
                + "  left join public.m_kota kt on kb.id_kota = kt.id\n"
                + "WHERE \n"
                + "     coalesce(kt.nama,'')||coalesce(kp.nama,'') ilike '%" + cari + "%' \n"
                + "     and case when " + idKapal + " = 0 then true else kp.id = " + idKapal + " end\n"
                + "     and case when " + idKota + " = 0 then true else kt.id = " + idKota + " end"
                + "     and "
                + "  kb.tgl_berangkat between '" + tglAwal + "'::date and '" + tglAkhir + "'::date\n" + ""
                + " order by kb.tgl_berangkat, kt.nama\n";
        System.out.println("query all : " + query);
        Integer totalElement = mr.countRecordset(query);
        int totalPages = totalElement == 0 ? 0 : totalElement <= page.getPageSize() ? 1
                : (totalElement / page.getPageSize()) + (totalElement % page.getPageSize() >= 1 ? 1 : 0);
        boolean isFirstPage = totalPages == 0 || page.getPageNumber() == 0;
        boolean isLastPage = totalPages == 0 || page.getPageNumber() == 0;

        query = query + "limit " + page.getPageSize() + "\n"
                + "offset " + (page.getOffset());
        System.out.println("query paging : " + query);
        logger.warn("Query [{}]", query);
        mm.addAttribute("content", mr.mapList(query));
        mm.put("size", page.getPageSize());
        mm.put("totalElements", totalElement);
        mm.put("totalPages", totalPages);
        mm.put("number", page.getPageNumber());
        mm.put("firstPage", isFirstPage);
        mm.put("lastPage", isLastPage);
        return mm;
    }

    public Object lookupEmkl(String cari, String idKota, PageRequest page) {
        ModelMap mm = new ModelMap();
        System.out.println("Page.Size: " + page.getPageSize());
        System.out.println("Page.Offset: " + page.getOffset());
        System.out.println("cari: " + cari);
        String query = "SELECT \n"
                + "  m_emkl.id, \n"
                + "  m_emkl.nama, \n"
                + "  m_kota.nama as kota, \n"
                + "  m_emkl.alamat, \n"
                + "  m_emkl.kontak, \n"
                + "  m_emkl.telepon\n"
                + "FROM \n"
                + "  public.m_emkl \n"
                + "  left join  public.m_kota on m_emkl.id_kota = m_kota.id\n"
                + "WHERE \n"
                + "    coalesce(m_emkl.nama,'') || coalesce(m_kota.nama,'') || coalesce(m_emkl.alamat,'') \n"
                + "    || coalesce(m_emkl.kontak,'') ||  coalesce(m_emkl.telepon,'') ilike '%" + cari + "%' \n"
                + "    and case when " + idKota + " = 0 then true else m_emkl.id_kota = " + idKota + " end "
                + " order by m_emkl.nama \n";
        System.out.println("query all : " + query);
        Integer totalElement = mr.countRecordset(query);
        int totalPages = totalElement == 0 ? 0 : totalElement <= page.getPageSize() ? 1
                : (totalElement / page.getPageSize()) + (totalElement % page.getPageSize() >= 1 ? 1 : 0);
        boolean isFirstPage = totalPages == 0 || page.getPageNumber() == 0;
        boolean isLastPage = totalPages == 0 || page.getPageNumber() == 0;

        query = query + "limit " + page.getPageSize() + "\n"
                + "offset " + (page.getOffset());
        System.out.println("query paging : " + query);
        logger.warn("Query [{}]", query);
        mm.addAttribute("content", mr.mapList(query));
        mm.put("size", page.getPageSize());
        mm.put("totalElements", totalElement);
        mm.put("totalPages", totalPages);
        mm.put("number", page.getPageNumber());
        mm.put("firstPage", isFirstPage);
        mm.put("lastPage", isLastPage);
        return mm;
    }

    public Object lookupBahanSj(String cari, Boolean outstand, String tglAwal, String tglAkhir, PageRequest page) {
        ModelMap mm = new ModelMap();
        System.out.println("Page.Size: " + page.getPageSize());
        System.out.println("Page.Offset: " + page.getOffset());
        System.out.println("cari: " + cari);
        String query = "SELECT * from t_bahan_sj \n"
                + "WHERE \n"
                + "  coalesce(keterangan,'') ilike '%" + cari + "%' \n"
                + "  and "
                + "  tgl_ins between '" + tglAwal + "'::date and '" + tglAkhir + "'::date\n"
                + "  and "
                + "  case when " + outstand + " = true then id not in (select id_bahan from t_surat_jalan) else true end "
                + " order by tgl_ins\n";
        System.out.println("query all : " + query);
        Integer totalElement = mr.countRecordset(query);
        int totalPages = totalElement == 0 ? 0 : totalElement <= page.getPageSize() ? 1
                : (totalElement / page.getPageSize()) + (totalElement % page.getPageSize() >= 1 ? 1 : 0);
        boolean isFirstPage = totalPages == 0 || page.getPageNumber() == 0;
        boolean isLastPage = totalPages == 0 || page.getPageNumber() == 0;

        query = query + "limit " + page.getPageSize() + "\n"
                + "offset " + (page.getOffset());
        System.out.println("query paging : " + query);
        logger.warn("Query [{}]", query);
        mm.addAttribute("content", mr.mapList(query));
        mm.put("size", page.getPageSize());
        mm.put("totalElements", totalElement);
        mm.put("totalPages", totalPages);
        mm.put("number", page.getPageNumber());
        mm.put("firstPage", isFirstPage);
        mm.put("lastPage", isLastPage);
        return mm;
    }

    public Object lookupToko(String cari, String idKota, PageRequest page) {
        ModelMap mm = new ModelMap();
        System.out.println("Page.Size: " + page.getPageSize());
        System.out.println("Page.Offset: " + page.getOffset());
        System.out.println("cari: " + cari);
        String query = "SELECT \n"
                + "  m_toko.id, \n"
                + "  m_toko.nama, \n"
                + "  m_kota.nama as kota, \n"
                + "  m_toko.alamat, \n"
                + "  m_toko.kontak, \n"
                + "  m_toko.telepon\n"
                + "FROM \n"
                + "  public.m_toko \n"
                + "  left join  public.m_kota on m_toko.id_kota = m_kota.id\n"
                + "WHERE \n"
                + "    coalesce(m_toko.nama,'') || coalesce(m_kota.nama,'') || coalesce(m_toko.alamat,'') \n"
                + "    || coalesce(m_toko.kontak,'') ||  coalesce(m_toko.telepon,'') ilike '%" + cari + "%' \n"
                + "    and case when " + idKota + " = 0 then true else m_toko.id_kota = " + idKota + " end "
                + " order by m_toko.nama \n";
        System.out.println("query all : " + query);
        Integer totalElement = mr.countRecordset(query);
        int totalPages = totalElement == 0 ? 0 : totalElement <= page.getPageSize() ? 1
                : (totalElement / page.getPageSize()) + (totalElement % page.getPageSize() >= 1 ? 1 : 0);
        boolean isFirstPage = totalPages == 0 || page.getPageNumber() == 0;
        boolean isLastPage = totalPages == 0 || page.getPageNumber() == 0;

        query = query + "limit " + page.getPageSize() + "\n"
                + "offset " + (page.getOffset());
        System.out.println("query paging : " + query);
        logger.warn("Query [{}]", query);
        mm.addAttribute("content", mr.mapList(query));
        mm.put("size", page.getPageSize());
        mm.put("totalElements", totalElement);
        mm.put("totalPages", totalPages);
        mm.put("number", page.getPageNumber());
        mm.put("firstPage", isFirstPage);
        mm.put("lastPage", isLastPage);
        return mm;
    }

    public Object lookupSjStuffing(String idSjDetail) {
        ModelMap mm = new ModelMap();
        String query = "SELECT \n"
                + "  sjst.coli, \n"
                + "  sjst.id_sj_detail, \n"
                + "  sjst.id, \n"
                + "  sjst.id_stuffing, \n"
                + "  k_b.nomor as nomor_kontainer_b, \n"
                + "  kp_b.nama as nama_kapal_b, \n"
                + "  kb_b.tgl_berangkat as tgl_berangkat_b, \n"
                + "  sjd.id_surat_jalan, \n"
                + "  kb_a.tgl_berangkat as tgl_berangkat_a, \n"
                + "  kp_a.nama as nama_kapal_a, \n"
                + "  k_a.nomor as nomor_kontainer_a\n"
                + "FROM \n"
                + "  public.t_sj_stuffing sjst \n"
                + "  left join public.t_stuffing st_b on sjst.id_stuffing = st_b.id \n"
                + "  left join public.t_kapal_berangkat kb_b on st_b.id_kapal_berangkat = kb_b.id \n"
                + "  left join public.m_kapal kp_b on kb_b.id_kapal = kp_b.id\n"
                + "  left join public.m_kontainer k_b on st_b.id_kontainer = k_b.id\n"
                + "  left join public.t_surat_jalan_detail sjd on sjst.id_sj_detail = sjd.id\n"
                + "  left join public.t_surat_jalan sj on sjd.id_surat_jalan = sj.id\n"
                + "  left join public.t_stuffing st_a on sj.id_stuffing = st_a.id\n"
                + "  left join public.t_kapal_berangkat kb_a on st_a.id_kapal_berangkat = kb_a.id  \n"
                + "  left join public.m_kapal kp_a on kb_a.id_kapal = kp_a.id \n"
                + "  left join public.m_kontainer k_a on st_a.id_kontainer = k_a.id\n"
                + "WHERE \n"
                + "  sjst.id_sj_detail = " + idSjDetail;
        return mr.mapList(query);
    }
}
