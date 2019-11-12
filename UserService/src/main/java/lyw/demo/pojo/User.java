package lyw.demo.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "user")
@ToString
@Getter
@Setter
public class User implements Serializable {
    @Id
    private Integer id;

    private String username;

    private String phonenumber;

    private String email;

    private String password;

    private String level;

}
