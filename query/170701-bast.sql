CREATE OR REPLACE FUNCTION public.fn_nota_bulan_romawi(integer)
  RETURNS character varying AS
$BODY$
declare 
	v_romawi varchar;
begin
	select into v_romawi case 
		when $1=1 then 'I'
		when $1=2 then 'II'
		when $1=3 then 'III'
		when $1=4 then 'IV'
		when $1=5 then 'V'
		when $1=6 then 'VI'
		when $1=7 then 'VII'
		when $1=8 then 'VIII'
		when $1=9 then 'IX'
		when $1=10 then 'X'
		when $1=11 then 'XI'
		when $1=12 then 'XII'
	end;
return v_romawi;
--select fn_nota_bulan_romawi(12)
end
$BODY$
  LANGUAGE plpgsql VOLATILE;
  
CREATE TABLE public.head_note
(
  head_note text NOT NULL,
  claim_note text,
  active boolean DEFAULT true,
  telp1 character varying(15),
  telp2 character varying(15),
  fax character varying(15),
  email character varying(100),
  alamat text,
  CONSTRAINT head_note_pkey PRIMARY KEY (head_note)
);

insert into head_note(head_note, claim_note, active, telp1, telp2, fax, email, alamat)
VALUES(
'Melayani Tujuan : Merauke, Timika, Manokwari, Jayapura',
'Batas Claim Kehilangan/Kerusakan Barang Max 1 Bulan Setelah Barang Diterima. Mohon Pemilik Barang Mengasuransikan Barangnya. Barang Yang Tidak Diasuransikan, Apabila Ada Musibah Dari Pihak Expedisi Tidak Menerima Claim.',
true, 
'031-5310360',
'031-5477951',
'031-5473590',
'transpapua@yahoo.co.id',
'JL. DUPAK  NO 65 C - 17');

create table bast(
	id_kapal_berangkat int references t_kapal_berangkat(id),
	id_merk int references m_merk(id),
	id_emkl int references m_emkl(id),
	nomor varchar(30) primary key,
	unique(id_kapal_berangkat, id_merk, id_emkl)
);

CREATE OR REPLACE FUNCTION fn_bast_get_nomor(int)
  RETURNS character varying AS
$BODY$
declare
	v_singkatan varchar;
	v_new_kode varchar;
	r_kapal	record;
begin	
select into r_kapal 
tgl_berangkat, k.kode_nota
from t_kapal_berangkat kb 
inner join m_kota k on k.id=kb.id_kota
where kb.id=$1;

select into v_new_kode 
trim(to_char(max(substring(nomor, 1,3))::int+1, '000'))||'/BA/TPJ-'||r_kapal.kode_nota||'/'||fn_nota_bulan_romawi(to_char(r_kapal.tgl_berangkat, 'MM')::int)||'/'||to_char(r_kapal.tgl_berangkat, 'yy') 
from bast
where id_kapal_berangkat=$1;

return coalesce(v_new_kode, '001/BA/TPJ-'||r_kapal.kode_nota||'/'||fn_nota_bulan_romawi(to_char(r_kapal.tgl_berangkat, 'MM')::int)||'/'||to_char(r_kapal.tgl_berangkat, 'yy')); 
--select fn_bast_get_nomor(1)
end
$BODY$
  LANGUAGE plpgsql VOLATILE

CREATE OR REPLACE FUNCTION public.fn_rpt_bast(
    integer, integer, integer)
  RETURNS SETOF record AS
$BODY$
declare
	v_kapal alias for $1;
	v_merk 	alias for $2;
	v_emkl 	alias for $3;
	v_head_note text;
	v_claim_note text;
	r record;
	v_new_nomor varchar;
begin

select into v_new_nomor 
nomor From bast
where id_merk=v_merk and id_kapal_berangkat=v_kapal and id_emkl=v_emkl;

if v_new_nomor is null then
	select into v_new_nomor fn_bast_get_nomor(v_kapal);
	insert into bast(id_kapal_berangkat, id_merk, id_emkl, nomor) 
	VALUES (v_kapal, v_merk, v_emkl, v_new_nomor);
end if;

select into v_head_note, v_claim_note coalesce(head_note,''), coalesce(claim_note, '') from head_note where active=true;

v_head_note:=coalesce(v_head_note, '');
v_claim_note:=coalesce(v_claim_note, '');


for r IN	

	select v_new_nomor::varchar nomor, coalesce(kt.nama,'') kota_tujuan, coalesce(k.nama,'') kondisi,
	--v_toko, --coalesce(t.nama,'') customer, 
	coalesce(kp.nama,'') kapal, kb.tgl_berangkat, 
	fn_tanggal_ind(kb.tgl_berangkat) tgl_ind, (coalesce(m.nama,'')||coalesce(' ('||t.nama||')',''))::varchar merk, coalesce(t.alamat,'') alamat_customer, 
	coalesce(kn.nomor,'') nomor_kontainer, coalesce(e.nama,'') emkl, 
	sj.id, sj.tanggal, coalesce(pengirim.nama,'') pengirim, coalesce(ss.coli,0) coli, 
	coalesce(i.nama,'')
-- 	||case when trim(coalesce(d.spesifikasi,''))<>'' then E'\n'||d.spesifikasi else '' end  
-- 	||case when kon.kont=1 then '' else E'\n'||fn_ket_kontainer_pisah(d.id, ss.id_stuffing) end jenis_barang, 
	||case when trim(coalesce(d.spesifikasi,''))<>'' then E'\n'||coalesce(d.spesifikasi,'') else '' end
	||case when trim(coalesce(d.catatan,''))<>'' then E'\n'||'<style isBold="true">'||coalesce(d.catatan,'')||'</style>' else '' end
	||coalesce(fn_ket_kontainer_pisah(ss.id_sj_detail,ss.id_stuffing),'') as  jenis_barang,
	coalesce(d.p,0) p, coalesce(d.l,0) l, coalesce(d.t,0) t, 
	coalesce(d.paket,false) as paket, 
	case when d.paket=true then 'Paket' 
		when d.paket=false and (d.fix_volume > 0 or (coalesce(d.p,0)=0 and coalesce(d.l,0)=0 and coalesce(d.t,0)=0)) then '0'
		else case when coalesce(d.p,0)>0 and coalesce(d.l,0)=0 and coalesce(d.t,0)=0 then coalesce(d.p,0)::numeric(18,3)::varchar else 
			     case when d.p>0 then d.p::varchar else '' end|| case when d.l>0 then ' x ' else '' end ||
			     case when d.l>0 then d.l::varchar else '' end || case when d.t>0 then ' x ' else '' end ||
			     case when d.t>0 then d.t::varchar else '' end
		     end
		end ukuran,   
	
	case when not coalesce(d.paket, false) and d.fix_volume>0 then d.fix_volume else
	coalesce(ss.coli,0) * 
		case 	when d.p>0 and coalesce(d.l,0)=0 and coalesce(d.t,0)=0 then d.p
			when d.p>0 and d.l>0 and coalesce(d.t,0)=0 then d.p*d.l/1000
			when d.p>0 and d.l>0 and d.t>0 then d.p*d.l*d.t/1000000 else 0 end end::numeric(18,4) as kubikasi,
	d.fix_volume, tot.total_coli||' Coli '||fn_ket_kontainer_pisah_sj(sj.id,ss.id_stuffing), 
	coalesce(sj_pisah.pisah, false) or coalesce(det_pisah.pisah, false), sk.nama as satuan_kirim, coalesce(kp_kn.jml_kontainer,0) jml_kontainer,
	v_head_note, v_claim_note		
	from t_sj_stuffing ss 
	inner join t_surat_jalan_detail d on d.id=ss.id_sj_detail
	inner join t_surat_jalan sj on d.id_surat_jalan=sj.id
	inner join t_stuffing st on st.id=ss.id_stuffing
	inner join m_satuan_kirim sk on sk.id=st.id_satuan_kirim
	inner join m_kontainer kn on kn.id=st.id_kontainer
	inner join m_kota kt on kt.id=st.id_kota
	inner join m_merk m on m.id=sj.id_merk
	inner join m_toko t on t.id=m.id_toko
	inner join m_item i on i.id=d.id_item
	left join m_emkl e on e.id=st.id_emkl
	left join m_kondisi k on k.id=sj.id_kondisi
	left join m_toko pengirim on pengirim.id=sj.id_toko
	left join t_kapal_berangkat kb on kb.id=st.id_kapal_berangkat
	left join m_kapal kp on kp.id=kb.id_kapal
	left join (
		select s.id_sj_detail, count(s2.id_sj_detail) kont 
		from t_sj_stuffing s 
		left join t_sj_stuffing s2 on s.id_sj_detail=s2.id_sj_detail
-- 		where s.id_stuffing=7
		group by s.id_sj_detail
	) kon on kon.id_sj_detail=d.id
	left join (
		select id_surat_jalan, sum(coli) as total_coli from t_surat_jalan_detail group by id_surat_jalan
	) tot on tot.id_surat_jalan = sj.id	
	left join (		
		select id, bool_or(is_pisah) as pisah from (
			select ss.id_sj_detail, sj.id, case when count(*) > 1 then true else false end as is_pisah  
			from t_sj_stuffing  ss
			inner join t_surat_jalan_detail d on d.id=ss.id_sj_detail
			inner join t_surat_jalan sj on d.id_surat_jalan=sj.id
			where sj.id_merk = v_merk
			group by ss.id_sj_detail, sj.id
		) x group by id
	) det_pisah on det_pisah.id = sj.id
	left join (		
		select id, bool_or(is_pisah) as pisah from (
			select sj.id, case when count(distinct ss.id_stuffing) > 1 then true else false end as is_pisah  
			from t_sj_stuffing  ss
			inner join t_surat_jalan_detail d on d.id=ss.id_sj_detail
			inner join t_surat_jalan sj on d.id_surat_jalan=sj.id
			where sj.id_merk = v_merk
			group by sj.id
		) x group by id
	) sj_pisah on sj_pisah.id = sj.id
	left join (
		select * from fn_pl_merk_jumlah_kontainer_per_kapal(v_kapal, ARRAY[v_merk]) as (id_merk integer, jml_kontainer bigint)
	)kp_kn on kp_kn.id_merk=sj.id_merk
	where st.id_kapal_berangkat=v_kapal and coalesce(ss.coli,0) > 0
	and sj.id_merk = v_merk
	order by coalesce(kn.nomor,''), case when sk.nama='LCL' then 1 else 0 end, sj.id, d.id
	
LOOP
	return next r;
END LOOP;
/*
select * From fn_rpt_bast(2969, 1, 5) as (nomor varchar, kota_tujuan varchar, kondisi varchar, 
kapal varchar, tgl_berangkat date, tgl_ind varchar, merk varchar, alamat varchar, nomor_kontainer varchar, emkl varchar, 
id integer, tanggal date, pengirim varchar, coli integer, jenis_barang text, p double precision, l double precision, 
t double precision, paket boolean , ukuran text, kubikasi numeric, fix_volume double precision, total_coli_sj text, pisah boolean, satuan_kirim varchar, 
jml_kontainer bigint, head_note text, claim_note text)

select * From fn_rpt_bast(1, 2969, 5) as (nomor varchar, kota_tujuan varchar, kondisi varchar, 
kapal varchar, tgl_berangkat date, tgl_ind varchar, merk varchar, alamat varchar, nomor_kontainer varchar, emkl varchar, 
id integer, tanggal date, pengirim varchar, coli integer, jenis_barang text, p double precision, l double precision, 
t double precision, paket boolean , ukuran text, kubikasi numeric, fix_volume double precision, total_coli_sj text, pisah boolean, satuan_kirim varchar, 
jml_kontainer bigint, head_note text, claim_note text)


*/

end
$BODY$
  LANGUAGE plpgsql VOLATILE;

  CREATE OR REPLACE FUNCTION public.fn_pl_rpt_rekap_merk_per_stuffing(integer)
  RETURNS SETOF record AS
$BODY$

declare
	v_id	alias for $1;
	rcd	record;
begin
for rcd in	
	select coalesce(kn.nomor,'') nomor_kontainer, coalesce(kp.nama,'')|| ' , Berangkat Tgl: '||fn_tanggal_ind(kb.tgl_berangkat) kapal, 
	coalesce(e.nama,'') emkl, (coalesce(m.nama,'')||coalesce(' ('||t.nama||')',''))::varchar merk, 
	sum(coalesce(ss.coli,0)) coli 
	from t_sj_stuffing ss 
	inner join t_surat_jalan_detail d on d.id=ss.id_sj_detail
	inner join t_surat_jalan sj on d.id_surat_jalan=sj.id
	inner join t_stuffing st on st.id=ss.id_stuffing
	inner join m_kontainer kn on kn.id=st.id_kontainer
	inner join m_kota kt on kt.id=st.id_kota
	inner join m_merk m on m.id=sj.id_merk
	inner join m_toko t on t.id=m.id_toko
	left join m_emkl e on e.id=st.id_emkl
	left join t_kapal_berangkat kb on kb.id=st.id_kapal_berangkat
	left join m_kapal kp on kp.id=kb.id_kapal
	where ss.id_stuffing=v_id and coalesce(ss.coli,0) > 0
	group by coalesce(kn.nomor,''), coalesce(kp.nama,'')|| ' , Berangkat Tgl: '||fn_tanggal_ind(kb.tgl_berangkat), 
	coalesce(e.nama,'') , (coalesce(m.nama,'')||coalesce(' ('||t.nama||')',''))
	order by (coalesce(m.nama,'')||coalesce(' ('||t.nama||')',''))
loop
	return next rcd;
END LOOP;	
/*
select * from fn_pl_rpt_rekap_merk_per_stuffing(2)as (no_kontainer varchar, kapal text, emkl varchar, merk varchar, coli bigint)
*/

end

$BODY$
  LANGUAGE plpgsql VOLATILE;