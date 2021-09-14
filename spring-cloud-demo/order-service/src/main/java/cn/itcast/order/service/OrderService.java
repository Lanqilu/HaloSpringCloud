package cn.itcast.order.service;

import cn.itcast.feign.clients.UserClient;
import cn.itcast.order.mapper.OrderMapper;
import cn.itcast.feign.pojo.Order;
import cn.itcast.feign.pojo.User;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserClient userClient;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2. 使用 Feign 进行远程调用
        User user = userClient.findById(order.getUserId());
        // 3. 封装 user 到 Order
        order.setUser(user);
        // 4.返回
        return order;
    }

    @SentinelResource("goods")
    public void queryGoods(){
        System.err.println("查询商品");
    }
}
