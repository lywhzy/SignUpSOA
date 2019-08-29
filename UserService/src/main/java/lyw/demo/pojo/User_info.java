package lyw.demo.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "user_info")
public class User_info {
    private Integer id;

    private String number;

    private String faculty;

    private String name;

    private String clazz;

    private String profession;

    private Date enrolment;

    private Integer picture;

}