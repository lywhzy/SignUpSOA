package lyw.demo.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "contest")
@ToString
public class Contest implements Serializable {

    @Id
    private Integer id;

    private String name;

    private String characterization;

    private Integer attachment;

    private Date begindate;

    private Date enddate;


    @Transient
    private List<User> users;
    @Transient
    private String status;
    @Transient
    private String display;
}