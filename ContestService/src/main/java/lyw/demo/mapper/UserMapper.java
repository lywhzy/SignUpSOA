package lyw.demo.mapper;

import lyw.demo.pojo.User;
import lyw.demo.pojo.User_info;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    @Select("select * from user_info where id = #{uid}")
    User_info selectInfo(Integer uid);
}
