package com.ta.prediksireksadanaarima.models

import com.ta.prediksireksadanaarima.models.RDProductModel

class RDProductListModel {
    companion object{
        fun getList(): ArrayList<RDProductModel> {
            val list = ArrayList<RDProductModel>()
            list.add(RDProductModel("RD68","Avrist Ada Saham Blue Safir"))
            list.add(RDProductModel("RD82","Avrist IDX30"))
            list.add(RDProductModel("RD3672","Bahana Primavera 99 Kelas G"))
            list.add(RDProductModel("RD216","Batavia Dana Saham"))
            list.add(RDProductModel("RD218","Batavia Dana Saham Syariah"))
            list.add(RDProductModel("RD334","BNI-AM Dana Saham Inspiring Equity Fund"))
            list.add(RDProductModel("RD337","BNI-AM Indeks IDX30"))
            list.add(RDProductModel("RD2049","BNI-AM Mahogany"))
            list.add(RDProductModel("RD409","BNP Paribas Ekuitas"))
            list.add(RDProductModel("RD412","BNP Paribas Infrastruktur Plus"))
            list.add(RDProductModel("RD423","BNP Paribas Pesona"))
            list.add(RDProductModel("RD424","BNP Paribas Pesona Syariah"))
            list.add(RDProductModel("RD431","BNP Paribas Solaris"))
            list.add(RDProductModel("RD1911","BNP Paribas SRI KEHATI"))
            list.add(RDProductModel("RD562","Danareksa Indeks Syariah"))
            list.add(RDProductModel("RD569","Danareksa Mawar Konsumer 10 Kelas A"))
            list.add(RDProductModel("RD3509","Eastspring Investments Value Discovery Kelas A"))
            list.add(RDProductModel("RD833","Majoris Saham Alokasi Dinamik Indonesia"))
            list.add(RDProductModel("RD853","Mandiri Investa Atraktif-Syariah"))
            list.add(RDProductModel("RD985","Manulife Dana Saham Kelas A"))
            list.add(RDProductModel("RD998","Manulife Saham Andalan"))
            list.add(RDProductModel("RD1001","Manulife Syariah Sektoral Amanah Kelas A"))
            list.add(RDProductModel("RD707","Principal Index IDX30 Kelas O"))
            list.add(RDProductModel("RD487","Principal Islamic Equity Growth Syariah"))
            list.add(RDProductModel("RD1538","Schroder 90 Plus Equity Fund"))
            list.add(RDProductModel("RD1541","Schroder Dana Istimewa"))
            list.add(RDProductModel("RD1628","Simas Saham Unggulan"))
            list.add(RDProductModel("RD1634","Simas Syariah Unggulan"))
            list.add(RDProductModel("RD1653","Sucorinvest Equity Fund"))
            list.add(RDProductModel("RD1656","Sucorinvest Maxi Fund"))
            list.add(RDProductModel("RD1668","Sucorinvest Sharia Equity Fund"))
            list.add(RDProductModel("RD1755","TRAM Consumption Plus Kelas A"))
            list.add(RDProductModel("RD1756","TRAM Infrastructure Plus"))
            list.add(RDProductModel("RD1764","TRIM Kapital"))
            list.add(RDProductModel("RD1765","TRIM Kapital Plus"))
            list.add(RDProductModel("RD1366","TRIM Syariah Saham"))
            return list
        }
    }
}