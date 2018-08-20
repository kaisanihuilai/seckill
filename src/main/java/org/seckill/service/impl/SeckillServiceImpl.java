package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    //md5盐值
    private final String slat = "akdhfkaj.;egrej!#$%^gjelfdhfivu";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(1,4);
    }

    @Override
    public Seckill getById(long seckillid) {
        return seckillDao.queryByid(seckillid);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryByid(seckillId);
        if (seckill == null){
            return new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime()<startTime.getTime() || nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //特定化字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点：
     * 1：开发团队达成一致约定，明确标注事务方法的编程风格
     * 2：
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 ==null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("秒杀时间还没到");
        }
        Date nowTime = new Date();
        //执行秒杀操作，减库存
        int updateCount = seckillDao.reduceNumber(seckillId,nowTime);

        try {
            if (updateCount<=0){
                //没有更新记录，秒杀结束
                throw new SeckillCloseException("秒杀结束");
            }else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if(insertCount <=0){
                    throw new RepeatKillException("重复秒杀");
                }else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1) {
            throw e1;
        }catch (RepeatKillException e2) {
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }
    }

    private  String getMD5(long seckillId){
        String base = seckillId+"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
