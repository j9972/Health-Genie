package com.example.healthgenie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommonService {

    public static int collectionSizeCheck(Collection collection){
        return collection.size();
    }

    public static boolean isNull(Object object){
        if(object==null){
            return true;
        }
        else{
            return false;
        }
    }
}
