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
}
