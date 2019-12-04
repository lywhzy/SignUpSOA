package lyw.demo.mapper;

import lyw.demo.pojo.Column_value;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface Column_valueMapper extends Mapper<Column_value> {

    @Delete("delete from user_contest where #{cid}=cid")
    void deleteContestRelation(@Param("cid") int cid);
}
