package lyw.demo.RabbitMQ;


import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class Recieve {

    private ConcurrentHashMap<String,String> stringStringConcurrentHashMap;

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "ab", durable = "true"),
//            exchange = @Exchange(
//                    value = "springExchange",type = ExchangeTypes.DIRECT,ignoreDeclarationExceptions = "true"
//            ),
//            key = {"ab"}))
//    public void listen(String msg){
//
//    }
}
