package com.cqut.atao.farm.order.application.process;


import com.cqut.atao.farm.order.application.process.req.CreateOrderReq;

/**
 * @author atao
 * @version 1.0.0
 * @ClassName CreateOrder.java
 * @Description 创建订单流程接口
 * @createTime 2023年02月04日 15:32:00
 */
public interface CreateOrderProcess {


    /**
     * 创建订单
     * @param req {@link CreateOrderReq}
     * @return 订单号
     */
    String createOrder(CreateOrderReq req);

}
