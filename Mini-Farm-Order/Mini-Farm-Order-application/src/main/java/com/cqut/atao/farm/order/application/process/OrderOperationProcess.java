package com.cqut.atao.farm.order.application.process;


import com.cqut.atao.farm.order.domain.model.aggregate.Order;

/**
 * @author atao
 * @version 1.0.0
 * @ClassName CreateOrder.java
 * @Description 创建订单流程接口
 * @createTime 2023年02月04日 15:32:00
 */
public interface OrderOperationProcess {


    /**
     * 创建订单
     * @param order {@link Order}
     * @return 订单号
     */
    String createOrder(Order order);

    /**
     * 取消订单
     * @param orderNo 订单号
     */
    void cancelOrder(String orderNo);

}