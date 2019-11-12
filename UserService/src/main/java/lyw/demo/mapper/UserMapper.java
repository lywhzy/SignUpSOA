package lyw.demo.mapper;

import lyw.demo.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    /*@Select("select * from user where username=#{username} and password=#{password}")
    User selLogin(@Param("username") String username, @Param("password") String password);*/

    //@Select("select username from user where ")

    @Insert("insert into user(username, phonenumber, email, password, level) values(#{record.username},#{user.phonenumber},#{email},#{password},#{level})")
    int insertUser(User record);

    @Select("select * from user where username=#{username}")
    User selLogin(String username);
}
