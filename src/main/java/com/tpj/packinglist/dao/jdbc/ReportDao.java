/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ustadho
 */
@Repository
public class ReportDao {

    @Autowired
    MapResultSet mr;

    public Object perStuffing(Integer id) {
        String sql = "select kota_tujuan, kondisi, \n"
                + "customer, kapal, tgl_berangkat, tgl_ind, merk, alamat, nomor_kontainer, emkl, \n"
                + "id, tanggal, pengirim, coli, jenis_barang, p, l, \n"
                + "t, paket, ukuran, kubikasi, fix_volume, total_coli_sj, pisah from fn_pl_rpt_per_stuffing(" + id + ") as (kota_tujuan varchar, kondisi varchar,\n"
                + "customer varchar, kapal varchar, tgl_berangkat date, tgl_ind varchar, merk varchar, alamat varchar, nomor_kontainer varchar, emkl varchar,\n"
                + "id integer, tanggal date, pengirim varchar, coli integer, jenis_barang text, p double precision, l double precision,\n"
                + "t double precision, paket boolean , ukuran text, kubikasi numeric, fix_volume double precision, total_coli_sj text, pisah boolean)";
        return mr.mapList(sql);
    }

    public Object perKapalToko(Integer idKapal, Integer idToko) {
        String sql = "select kota_tujuan, kondisi, \n"
                + "customer, kapal, tgl_berangkat, tgl_ind, merk, alamat, nomor_kontainer, emkl, \n"
                + "id, tanggal, pengirim, coli, jenis_barang, p, l, \n"
                + "t, paket, case when ukuran not ilike '%x%' and ukuran not ilike '%paket%' and ukuran not ilike '' then '0'::text else ukuran end as ukuran, kubikasi, fix_volume, total_coli_sj, pisah, satuan_kirim  from fn_pl_rpt_per_kapal_toko(" + idKapal + ", " + idToko + ") as (kota_tujuan varchar, kondisi varchar, \n"
                + "customer varchar, kapal varchar, tgl_berangkat date, tgl_ind varchar, merk varchar, alamat varchar, nomor_kontainer varchar, emkl varchar, \n"
                + "id integer, tanggal date, pengirim varchar, coli integer, jenis_barang text, p double precision, l double precision, \n"
                + "t double precision, paket boolean ,ukuran text, kubikasi numeric, fix_volume double precision, total_coli_sj text, pisah boolean, satuan_kirim varchar)";
        return mr.mapList(sql);
    }

    public Object perKapalMerkToko(Integer idKapal, String idMerkToko) {
        //case when ukuran not ilike '%x%' and ukuran not ilike '%paket%' and ukuran not ilike '' then '0'::text else ukuran end as 
        String sql = "select kota_tujuan, kondisi, \n"
                + "customer, kapal, tgl_berangkat, tgl_ind, merk, alamat, nomor_kontainer, emkl, \n"
                + "id, tanggal, pengirim, coli, jenis_barang, p, l, \n"
                + "t, paket, ukuran, "
                + "kubikasi, fix_volume, total_coli_sj, pisah, satuan_kirim, jml_kontainer  "
                + "from fn_pl_rpt_per_kapal_merk_toko(" + idKapal + ", ARRAY[" + idMerkToko + "]) as (kota_tujuan varchar, kondisi varchar, \n"
                + "customer varchar, kapal varchar, tgl_berangkat date, tgl_ind varchar, merk varchar, alamat varchar, nomor_kontainer varchar, emkl varchar, \n"
                + "id integer, tanggal date, pengirim varchar, coli integer, jenis_barang text, p double precision, l double precision, \n"
                + "t double precision, paket boolean ,ukuran text, kubikasi numeric, fix_volume double precision, total_coli_sj text, pisah boolean, satuan_kirim varchar, "
                + "jml_kontainer bigint)";
        System.out.println("perKapalMerkToko: " + sql);
        return mr.mapList(sql);
    }

    public Object jmlContainerPerTujuan(String tahun, String bulan) {
        String sql = "select * from fn_jml_container_per_tujuan(" + tahun + ", " + bulan + ") as (jml_container bigint, id_kota int, kota varchar, tahun int, bulan varchar)";
        System.out.println("jmlContainerPerTujuan: " + sql);
        return mr.mapList(sql);
    }
    
    public Object listBast(Integer idkb) {
        String sql = "select distinct sj.id_merk, m.nama merk, t.nama toko, st.id_emkl, e.nama emkl, bast.nomor\n" +
                    "from t_kapal_berangkat kb \n" +
                    "inner join t_stuffing st on st.id_kapal_berangkat=kb.id \n" +
                    "inner join m_kontainer kn on kn.id=st.id_kontainer\n" +
                    "inner join m_emkl e on e.id=st.id_emkl\n" +
                    "inner join m_satuan_kirim sk on sk.id=st.id_satuan_kirim\n" +
                    "inner join t_sj_stuffing ss on ss.id_stuffing=st.id\n" +
                    "inner join t_surat_jalan_detail sd on sd.id=ss.id_sj_detail\n" +
                    "inner join t_surat_jalan sj on sj.id=sd.id_surat_jalan\n" +
                    "inner join m_merk m on m.id=sj.id_merk\n" +
                    "inner join m_toko t on t.id=m.id_toko\n" +
                    "left join bast on bast.id_kapal_berangkat=kb.id and bast.id_merk=m.id and bast.id_emkl=e.id \n" +
                    "where kb.id="+idkb+"\n" +
                    "order by e.nama, m.nama";
        return mr.mapList(sql);
    }

    /**
     *
     * @param grup contoh : 'kota_tujuan', 'kondisi', 'customer', 'kapal',
     * 'nomor_kontainer', 'emkl', 'pengirim'
     * @param tglMulai format : yyyy-mm-dd
     * @param tglSampai format : yyyy-mm-dd
     * @param order contoh : 'grup_asc', 'grup_desc', 'coli_asc', 'coli_desc',
     * 'kubikasi_asc', 'kubikasi_desc'
     * @return
     */
    public Object rekapColiKubikasiPerGrup(String grup, String tglMulai, String tglSampai, String order, int limit) {
        String sql = "select * from fn_rekap_by_grup('" + grup + "'," + (tglMulai == null ? "null" : "'" + tglMulai + "'") + "," + (tglSampai == null ? "null" : "'" + tglSampai + "'") + ",'" + order + "') as (grup varchar, coli bigint, kubikasi numeric) limit " + limit;
        return mr.mapList(sql);
    }

    public Object bast(Integer idKapalBerangkat, Integer idMerk, Integer idEmkl) {
        String sql="select * From fn_rpt_bast("+idKapalBerangkat+", "+idMerk+", "+idEmkl+") as (nomor varchar, kota_tujuan varchar, kondisi varchar, \n" +
                    "kapal varchar, tgl_berangkat date, tgl_ind varchar, merk varchar, alamat varchar, nomor_kontainer varchar, emkl varchar, \n" +
                    "id integer, tanggal date, pengirim varchar, coli integer, jenis_barang text, p double precision, l double precision, \n" +
                    "t double precision, paket boolean , ukuran text, kubikasi numeric, fix_volume double precision, total_coli_sj text, pisah boolean, satuan_kirim varchar, \n" +
                    "jml_kontainer bigint, head_note text, claim_note text)";
        return mr.mapList(sql);
    }

    public Object rekapMerk(Integer id) {
        String sql="select * from fn_pl_rpt_rekap_merk_per_stuffing("+id+")as (no_kontainer varchar, kapal text, emkl varchar, merk varchar, coli bigint)";
        return mr.mapList(sql);
    }
}
