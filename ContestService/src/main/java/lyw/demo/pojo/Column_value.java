package lyw.demo.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "column_value")
public class Column_value implements Serializable {
    @Id
    private Integer id;

    private String value;

    private Integer uid;

    private Integer cid;

}