package cn.itcast.seata.order.web;

import cn.itcast.seata.order.entity.SeataOrder;
import cn.itcast.seata.order.service.SeataOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 虎哥
 */
@RestController
@RequestMapping("order")
public class SeataOrderController {

    private final SeataOrderService orderService;

    public SeataOrderController(SeataOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Long> createOrder(SeataOrder order){
        Long orderId = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }
}
