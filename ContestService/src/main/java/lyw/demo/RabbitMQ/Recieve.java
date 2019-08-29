package lyw.demo.RabbitMQ;


import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Recieve {

    private ConcurrentHashMap<String,String> stringStringConcurrentHashMap;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ab", durable = "true"),
            exchange = @Exchange(
                    value = "springExchange",type = ExchangeTypes.DIRECT,ignoreDeclarationExceptions = "true"
            ),
            key = {"ab"}))
    public void listen(String msg){

    }
}
