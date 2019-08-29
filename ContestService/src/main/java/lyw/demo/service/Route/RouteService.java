package lyw.demo.service.Route;

import lyw.demo.service.Request.Request;

import java.util.concurrent.ArrayBlockingQueue;

public interface RouteService {
    void process(Request request);
    ArrayBlockingQueue<Request> getQueue(Integer id);
}
