package cn.itcast.seata.order.service;

import cn.itcast.seata.order.entity.SeataOrder;

public interface SeataOrderService {

    /**
     * 创建订单
     */
    Long create(SeataOrder order);
}