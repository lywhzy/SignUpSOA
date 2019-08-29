package lyw.demo.Listener;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.service.Request.RequestThreadPool;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@Slf4j
public class Listener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){
            return;
        }
        log.info("线程池初始化");
        RequestThreadPool.init();
    }
}
