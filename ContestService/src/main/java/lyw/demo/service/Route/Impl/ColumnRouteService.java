package lyw.demo.service.Route.Impl;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.service.Request.Request;
import lyw.demo.service.Request.Requestqueue;
import lyw.demo.service.Route.RouteService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
@Slf4j
public class ColumnRouteService implements RouteService {


    @Override
    public void process(Request request) {
        BlockingQueue<Request> blockingQueue = getQueue(request.getContestId());
        try {
            blockingQueue.put(request);
        } catch (InterruptedException e) {
            log.error("比赛Id队列已满",request.getContestId(),e);
        }
    }

    @Override
    public ArrayBlockingQueue<Request> getQueue(Integer id) {
        Requestqueue requestqueue = Requestqueue.getRequestqueue();
        String key = id.toString();
        int hashcode;
        int hash = (key==null) ? 0 : (hashcode=key.hashCode())^(hashcode>>>16);
        int index = (requestqueue.getQueueSize()-1)&hash;
        log.info(String.valueOf(index));
        return requestqueue.getQueueByIndex(index);
    }
}
