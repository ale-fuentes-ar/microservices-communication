package ar.fuentes.ale.productapi.modules.product.rabbitmq;

import ar.fuentes.ale.productapi.modules.product.dto.ProductStrockDTO;
import ar.fuentes.ale.productapi.modules.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ProductStockListener {

    private final ProductService productService;

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void receiveProductStockMessage(ProductStrockDTO productStrockDTO) throws JsonProcessingException {
        log.info("Resquest message {}", new ObjectMapper().writeValueAsString(productStrockDTO));
        productService.updateProductStock(productStrockDTO);

    }


}
