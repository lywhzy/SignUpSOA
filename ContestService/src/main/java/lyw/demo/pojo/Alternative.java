package lyw.demo.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "alternative")
public class Alternative implements Serializable {
    @Id
    private Integer id;

    private Integer cid;

    private String value;

    private Boolean user_permit;

}