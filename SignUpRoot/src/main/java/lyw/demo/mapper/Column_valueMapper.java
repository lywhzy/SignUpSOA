package lyw.demo.mapper;

import lyw.demo.pojo.Column_value;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface Column_valueMapper extends Mapper<Column_value> {

    @Delete("delete user_contest from user_contest,user where #{cid}=cid and user.id = user_contest.uid and user.level = 'ROLE_USER'")
    void deleteContestRelation(@Param("cid") int cid);

}
