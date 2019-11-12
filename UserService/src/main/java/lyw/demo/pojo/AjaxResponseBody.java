package lyw.demo.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AjaxResponseBody implements Serializable {

    private String status;
    private String msg;
    private Object result;
    private String jwtToken;
    private String uuid;

}
