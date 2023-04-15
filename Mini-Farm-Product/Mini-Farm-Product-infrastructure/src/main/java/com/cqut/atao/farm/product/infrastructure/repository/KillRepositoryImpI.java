package com.cqut.atao.farm.product.infrastructure.repository;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqut.atao.farm.product.domain.activity.kill.model.req.AddKillProductReq;
import com.cqut.atao.farm.product.domain.activity.kill.model.req.DeployActivityReq;
import com.cqut.atao.farm.product.domain.activity.kill.model.res.KillACtivityRes;
import com.cqut.atao.farm.product.domain.activity.repository.KillRepository;
import com.cqut.atao.farm.product.infrastructure.dao.KillInSecondsMapper;
import com.cqut.atao.farm.product.infrastructure.dao.SecondKillProductMapper;
import com.cqut.atao.farm.product.infrastructure.po.KillsInSeconds;
import com.cqut.atao.farm.product.infrastructure.po.SecondKillProduct;
import com.cqut.atao.farm.springboot.starter.common.toolkit.BeanUtil;
import com.cqut.atao.farm.springboot.starter.convention.exception.ServiceException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author atao
 * @version 1.0.0
 * @ClassName KillRepositoryImpI.java
 * @Description 秒杀仓储实现层
 * @createTime 2023年03月14日 15:35:00
 */
@Component
public class KillRepositoryImpI implements KillRepository {

    @Resource
    private KillInSecondsMapper killInSecondsMapper;

    @Resource
    private SecondKillProductMapper secondKillProductMapper;

    @Override
    public void createKill(DeployActivityReq req) {
        KillsInSeconds killsInSeconds = BeanUtil.convert(req, KillsInSeconds.class);
        int i = killInSecondsMapper.insert(killsInSeconds);
        Assert.isTrue(i > 0, () -> new ServiceException("保存秒杀场次失败"));
    }

    @Override
    public void addKillActivity(AddKillProductReq req) {
        SecondKillProduct killProduct = BeanUtil.convert(req, SecondKillProduct.class);
        int i = secondKillProductMapper.insert(killProduct);
        Assert.isTrue(i > 0, () -> new ServiceException("保存秒杀商品失败"));
    }

    @Override
    public List<KillACtivityRes> queryList() {
        LambdaQueryWrapper<KillsInSeconds> wrapper = Wrappers.lambdaQuery(KillsInSeconds.class)
                .gt(KillsInSeconds::getStartTime, new Date());
        List<KillsInSeconds> records = killInSecondsMapper.selectPage(new Page<>(0, 5), wrapper).getRecords();
        return BeanUtil.convert(records, KillACtivityRes.class);
    }

    @Override
    public void passProduct(Long id, Integer status) {
        secondKillProductMapper.unpdateStatus(id, status);
    }

    @Override
    public List<Long> queryKillProduct(Long killId) {
        LambdaQueryWrapper<SecondKillProduct> eq = Wrappers.lambdaQuery(SecondKillProduct.class)
                .eq(SecondKillProduct::getKillId, killId);
        List<Long> collect = secondKillProductMapper
                .selectList(eq)
                .stream()
                .map(e -> {
                    return e.getProductId();
                }).collect(Collectors.toList());
        return collect;
    }
}
