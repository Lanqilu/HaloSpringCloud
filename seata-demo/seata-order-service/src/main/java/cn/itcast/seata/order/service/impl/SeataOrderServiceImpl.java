package cn.itcast.seata.order.service.impl;

import cn.itcast.seata.order.client.AccountClient;
import cn.itcast.seata.order.client.StorageClient;
import cn.itcast.seata.order.entity.SeataOrder;
import cn.itcast.seata.order.mapper.SeataOrderMapper;
import cn.itcast.seata.order.service.SeataOrderService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 虎哥
 */
@Slf4j
@Service
public class SeataOrderServiceImpl implements SeataOrderService {

    private final AccountClient accountClient;
    private final StorageClient storageClient;
    private final SeataOrderMapper orderMapper;

    public SeataOrderServiceImpl(AccountClient accountClient, StorageClient storageClient, SeataOrderMapper orderMapper) {
        this.accountClient = accountClient;
        this.storageClient = storageClient;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public Long create(SeataOrder order) {
        // 创建订单
        orderMapper.insert(order);
        try {
            // 扣用户余额
            accountClient.deduct(order.getUserId(), order.getMoney());
            // 扣库存
            storageClient.deduct(order.getCommodityCode(), order.getCount());

        } catch (FeignException e) {
            log.error("下单失败，原因:{}", e.contentUTF8(), e);
            throw new RuntimeException(e.contentUTF8(), e);
        }
        return order.getId();
    }
}
