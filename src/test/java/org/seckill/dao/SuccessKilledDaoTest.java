package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        int num = successKilledDao.insertSuccessKilled(1000L,18330020022L);
        System.out.println(num);
    }

    @Test
    public void queryByIdWithSeckill() {
        //SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000L);
        //System.out.println(successKilled);
    }
}