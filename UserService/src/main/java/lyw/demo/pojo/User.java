package lyw.demo.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
@Table(name = "user")
@ToString
public class User implements Serializable {
    @Id
    private Integer id;

    private String name;

    private String phoneNumber;

    private String email;

    private String password;

    private Integer level;

}
