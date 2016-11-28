package com.truckcompany.web;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by Vladimir on 27.11.2016.
 */
public class Test {
    public static void main(String[] args){
        System.out.print(UUID.randomUUID().toString().replaceAll("-", EMPTY).length());
        //UUID.randomUUID().toString().replaceAll("-", EMPTY) ;
    }
}
