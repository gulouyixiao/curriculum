package com.curriculum;

import com.curriculum.model.dto.OrderDTO;
import com.curriculum.service.OrderMainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderMainTest {

    @Autowired
    private OrderMainService orderMainService;

    @Test
    public void test() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setPage(1L);
        orderDTO.setPageSize(10L);
        orderMainService.PageQuery(orderDTO);
    }
}
