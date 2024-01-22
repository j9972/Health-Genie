package com.example.healthgenie.boundedContext.email.service;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UniDomainService {
    public String findUniDomain(String schoolName) {
        HashMap<String, String> schoolDomains = new HashMap<>();

        // 학교 이름과 도메인 추가
        schoolDomains.put("가톨릭대학교", "catholic.ac.kr");
        schoolDomains.put("가야대학교", "gaya.ac.kr");
        schoolDomains.put("가천대학교", "gachon.ac.kr");
        schoolDomains.put("가톨릭관동대학교", "cku.ac.kr");
        schoolDomains.put("감리교신학대학교", "gks.ac.kr");
        schoolDomains.put("강릉원주대학교", "gwnu.ac.kr");
        schoolDomains.put("건국대학교", "konkuk.ac.kr");
        schoolDomains.put("건양대학교", "kyonggi.ac.kr");
        schoolDomains.put("경기대학교", "kyonggi.ac.kr");
        schoolDomains.put("경남과학기술대학교", "gntech.ac.kr");
        schoolDomains.put("경남대학교", "gnu.ac.kr");
        schoolDomains.put("경북대학교", "knu.ac.kr");
        schoolDomains.put("경북대학교 대학원", "knu.ac.kr");
        schoolDomains.put("경북도립대학교", "gbd.ac.kr");
        schoolDomains.put("경북보건대학교", "kmu.ac.kr");
        schoolDomains.put("경북전문대학교", "kbpc.ac.kr");
        schoolDomains.put("경북전문대학교 대학원", "kbpc.ac.kr");
        schoolDomains.put("경상교육대학교", "knue.ac.kr");
        schoolDomains.put("경성대학교", "ks.ac.kr");
        schoolDomains.put("경운대학교", "kw.ac.kr");
        schoolDomains.put("경인교육대학교", "kinu.ac.kr");
        schoolDomains.put("경인여자대학교", "kiwu.ac.kr");
        schoolDomains.put("경주대학교", "kju.ac.kr");
        schoolDomains.put("경희대학교", "khu.ac.kr");
        schoolDomains.put("계명대학교", "kmu.ac.kr");
        schoolDomains.put("고려대학교", "korea.ac.kr");
        schoolDomains.put("고신대학교", "kosin.ac.kr");
        schoolDomains.put("광신대학교", "ksu.ac.kr");
        schoolDomains.put("광운대학교", "kw.ac.kr");
        schoolDomains.put("국민대학교", "kookmin.ac.kr");
        schoolDomains.put("군산대학교", "kunsan.ac.kr");
        schoolDomains.put("극동대학교", "ku.ac.kr");
        schoolDomains.put("글로벌사이버대학교", "gc.ac.kr");
        schoolDomains.put("김천대학교", "kmu.ac.kr");
        schoolDomains.put("김해대학교", "khun.ac.kr");
        schoolDomains.put("김포대학교", "kpu.ac.kr");
        schoolDomains.put("나사렛대학교", "nazarine.ac.kr");
        schoolDomains.put("남부대학교", "nambu.ac.kr");
        schoolDomains.put("단국대학교", "dankook.ac.kr");
        schoolDomains.put("대구가톨릭대학교", "cu.ac.kr");
        schoolDomains.put("대구경북과학기술원", "dgist.ac.kr");
        schoolDomains.put("대구경북대학교", "dgut.ac.kr");
        schoolDomains.put("대구대학교", "daegu.ac.kr");
        schoolDomains.put("대구예술대학교", "daarts.ac.kr");
        schoolDomains.put("대구한의대학교", "dkumc.ac.kr");
        schoolDomains.put("대신대학교", "dsu.ac.kr");
        schoolDomains.put("대진대학교", "dju.ac.kr");
        schoolDomains.put("덕성여자대학교", "duksung.ac.kr");
        schoolDomains.put("동강대학교", "dgu.ac.kr");
        schoolDomains.put("동국대학교", "dongguk.ac.kr");
        schoolDomains.put("동덕여자대학교", "duksook.ac.kr");
        schoolDomains.put("동명대학교", "dongmyung.ac.kr");
        schoolDomains.put("동서대학교", "dongseo.ac.kr");
        schoolDomains.put("동신대학교", "dongsin.ac.kr");
        schoolDomains.put("동아대학교", "donga.ac.kr");
        schoolDomains.put("동양대학교", "tgu.ac.kr");
        schoolDomains.put("동의대학교", "deu.ac.kr");
        schoolDomains.put("동인천대학교", "diu.ac.kr");
        schoolDomains.put("루터대학교", "lut.ac.kr");
        schoolDomains.put("명지대학교", "myongji.ac.kr");
        schoolDomains.put("목원대학교", "mokwon.ac.kr");
        schoolDomains.put("목포가톨릭대학교", "mcu.ac.kr");
        schoolDomains.put("목포대학교", "mokpo.ac.kr");
        schoolDomains.put("목포해양대학교", "mmu.ac.kr");
        schoolDomains.put("배재대학교", "paju.ac.kr");
        schoolDomains.put("백석대학교", "baekseok.ac.kr");
        schoolDomains.put("부경대학교", "pknu.ac.kr");
        schoolDomains.put("부산가톨릭대학교", "buc.ac.kr");
        schoolDomains.put("부산교육대학교", "busan-e.ac.kr");
        schoolDomains.put("부산대학교", "pusan.ac.kr");
        schoolDomains.put("부산외국어대학교", "bufl.ac.kr");
        schoolDomains.put("부산장신대학교", "pusanjin.ac.kr");
        schoolDomains.put("부산정보대학교", "busan.ac.kr");
        schoolDomains.put("삼육대학교", "samuel.ac.kr");
        schoolDomains.put("상명대학교", "smu.ac.kr");
        schoolDomains.put("서강대학교", "sogang.ac.kr");
        schoolDomains.put("서경대학교", "seokyeong.ac.kr");
        schoolDomains.put("서울과학기술대학교", "seoultech.ac.kr");
        schoolDomains.put("서울교육대학교", "seoul-e.ac.kr");
        schoolDomains.put("서울기독대학교", "uos.ac.kr");
        schoolDomains.put("서울과학기술대학교", "seoultech.ac.kr");
        schoolDomains.put("서울사이버대학교", "scu.ac.kr");
        schoolDomains.put("서울시립대학교", "uos.ac.kr");
        schoolDomains.put("서울신학대학교", "sut.ac.kr");
        schoolDomains.put("서울여자간호대학교", "swc.ac.kr");
        schoolDomains.put("서울여자대학교", "swu.ac.kr");
        schoolDomains.put("서울한영대학교", "shu.ac.kr");
        schoolDomains.put("서울한영대학교 대학원", "shu.ac.kr");
        schoolDomains.put("서원대학교", "seowon.ac.kr");
        schoolDomains.put("선문대학교", "sunmoon.ac.kr");
        schoolDomains.put("성균관대학교", "skku.edu");
        schoolDomains.put("성신여자대학교", "sungshin.ac.kr");
        schoolDomains.put("세명대학교", "semeym.ac.kr");
        schoolDomains.put("세종대학교", "sejong.ac.kr");
        schoolDomains.put("세한대학교", "sehan.ac.kr");
        schoolDomains.put("송원대학교", "songwon.ac.kr");
        schoolDomains.put("수원가톨릭대학교", "swc.ac.kr");
        schoolDomains.put("수원대학교", "suwon.ac.kr");
        schoolDomains.put("숙명여자대학교", "sm.ac.kr");
        schoolDomains.put("순천대학교", "sunchon.ac.kr");
        schoolDomains.put("순천향대학교", "sch.ac.kr");
        schoolDomains.put("숭실대학교", "ssu.ac.kr");
        schoolDomains.put("신경대학교", "sungkyul.ac.kr");
        schoolDomains.put("신라대학교", "silla.ac.kr");
        schoolDomains.put("아주대학교", "ajou.ac.kr");
        schoolDomains.put("안동대학교", "andong.ac.kr");
        schoolDomains.put("안양대학교", "anyang.ac.kr");
        schoolDomains.put("여수대학교", "ysu.ac.kr");
        schoolDomains.put("연세대학교", "yonsei.ac.kr");
        schoolDomains.put("영남대학교", "ynu.ac.kr");
        schoolDomains.put("영남신학대학교", "ynu.ac.kr");
        schoolDomains.put("영산대학교", "ysu.ac.kr");
        schoolDomains.put("영산선학대학교", "ysu.ac.kr");
        schoolDomains.put("예수대학교", "yesu.ac.kr");
        schoolDomains.put("예원예술대학교", "ywca.ac.kr");
        schoolDomains.put("용인대학교", "yongin.ac.kr");
        schoolDomains.put("우석대학교", "woosuk.ac.kr");
        schoolDomains.put("우송대학교", "wsu.ac.kr");
        schoolDomains.put("울산과학기술원", "unist.ac.kr");
        schoolDomains.put("울산대학교", "ulsan.ac.kr");
        schoolDomains.put("원광대학교", "wonkwang.ac.kr");
        schoolDomains.put("원광디지털대학교", "wku.ac.kr");
        schoolDomains.put("위덕대학교", "wee.ac.kr");
        schoolDomains.put("인제대학교", "inje.ac.kr");
        schoolDomains.put("인천가톨릭대학교", "iccu.ac.kr");
        schoolDomains.put("인천대학교", "incheon.ac.kr");
        schoolDomains.put("인하대학교", "inha.ac.kr");
        schoolDomains.put("장로회신학대학교", "ptsem.ac.kr");
        schoolDomains.put("전남대학교", "jnu.ac.kr");
        schoolDomains.put("전북대학교", "jbnu.ac.kr");
        schoolDomains.put("전북도립대학교", "jjc.ac.kr");
        schoolDomains.put("정석대학교", "jungseok.ac.kr");
        schoolDomains.put("제주대학교", "jeju.ac.kr");
        schoolDomains.put("제주한라대학교", "hanla.ac.kr");
        schoolDomains.put("조선대학교", "chosun.ac.kr");
        schoolDomains.put("중부대학교", "chubu.ac.kr");
        schoolDomains.put("중앙대학교", "cau.ac.kr");
        schoolDomains.put("중앙승가대학교", "scu.ac.kr");
        schoolDomains.put("중원대학교", "jungwon.ac.kr");
        schoolDomains.put("총신대학교", "chosun.ac.kr");
        schoolDomains.put("충남대학교", "cnu.ac.kr");
        schoolDomains.put("충북대학교", "chungbuk.ac.kr");
        schoolDomains.put("침례신학대학교", "cbt.ac.kr");
        schoolDomains.put("칼빈대학교", "calvin.ac.kr");
        schoolDomains.put("한경대학교", "hankuk.ac.kr");
        schoolDomains.put("한국산업기술대학교", "kpu.ac.kr");
        schoolDomains.put("한국성서대학교", "kus.ac.kr");
        schoolDomains.put("한국열린사이버대학교", "ku.ac.kr");
        schoolDomains.put("한국외국어대학교", "hufs.ac.kr");
        schoolDomains.put("한국체육대학교", "knsu.ac.kr");
        schoolDomains.put("한국항공대학교", "kau.ac.kr");
        schoolDomains.put("한남대학교", "hannam.ac.kr");
        schoolDomains.put("한동대학교", "handong.edu");
        schoolDomains.put("한라대학교", "hanla.ac.kr");
        schoolDomains.put("한려대학교", "hanlyo.ac.kr");
        schoolDomains.put("한림대학교", "hallym.ac.kr");
        schoolDomains.put("한서대학교", "hanseo.ac.kr");
        schoolDomains.put("한성대학교", "hansung.ac.kr");
        schoolDomains.put("한세대학교", "hansei.ac.kr");
        schoolDomains.put("한신대학교", "hanshin.ac.kr");
        schoolDomains.put("한양대학교", "hanyang.ac.kr");
        schoolDomains.put("한양대학교(ERICA)", "hanyang.ac.kr");
        schoolDomains.put("한일장신대학교", "kris.ac.kr");
        schoolDomains.put("항공대학교", "kau.ac.kr");
        schoolDomains.put("협성대학교", "hs.ac.kr");
        schoolDomains.put("호남대학교", "honam.ac.kr");
        schoolDomains.put("호서대학교", "hosu.ac.kr");
        schoolDomains.put("호원대학교", "howon.ac.kr");
        schoolDomains.put("홍익대학교", "hongik.ac.kr");

        return schoolDomains.get(schoolName);
    }

    public boolean checkDomain(String email, String checkDomain) {

        int atIndex = email.indexOf('@');
        String domain = null;

        if (atIndex != -1) {
            domain = email.substring(atIndex + 1);
        }

        return domain.equals(checkDomain);
    }
}
