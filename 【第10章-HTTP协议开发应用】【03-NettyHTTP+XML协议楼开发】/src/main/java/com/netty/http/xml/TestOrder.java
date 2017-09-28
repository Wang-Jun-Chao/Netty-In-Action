package com.netty.http.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.netty.http.xml.pojo.Order;
import com.netty.http.xml.pojo.OrderFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Author: 王俊超
 * Date: 2017-09-28 07:47
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class TestOrder {
    private XmlMapper xmlMapper = new XmlMapper();

    private String encode2Xml(Order order) throws JsonProcessingException {
        return xmlMapper.writeValueAsString(order);
    }

    private Order decode2Order(String xmlStr) throws IOException {
        return xmlMapper.readValue(xmlStr, Order.class);
    }

    public static void main(String[] args) throws IOException {
        TestOrder test = new TestOrder();
        Order order = OrderFactory.create(123);
        String body = test.encode2Xml(order);
        System.out.println(body);
        Order order2 = test.decode2Order(body);
        System.out.println(order2);
    }
}
