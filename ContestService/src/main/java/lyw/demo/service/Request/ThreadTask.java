package lyw.demo.service.Request;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.service.Request.RequestImpl.ColumnSelectRequest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;


@Slf4j
public class ThreadTask implements Callable<Boolean> {

    private ArrayBlockingQueue<Request> queue;

    public ThreadTask(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {
        while(true){
            long startTime = System.currentTimeMillis();
            Request request = queue.take();

            if(request instanceof ColumnSelectRequest){
                log.info(Thread.currentThread().getName() + "select");
            }else{
                log.info(Thread.currentThread().getName() + "update");
            }
            request.process();
            log.info(Thread.currentThread().getName() + "运行" + String.valueOf(System.currentTimeMillis()-startTime));
        }
    }
}
