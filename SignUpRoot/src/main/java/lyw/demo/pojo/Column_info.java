package lyw.demo.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
@Table(name = "column_info")
public class Column_info implements Serializable {
    @Id
    private Integer id;

    private Integer cid;

    private Integer sequence;

    private String name;

    private String icontype;

    private Boolean choose;

    @Transient
    private String value;
    @Transient
    private List<String> alternatives;

    @Transient
    private List<Alternative> alternativeList;
}