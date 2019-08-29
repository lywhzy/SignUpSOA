package lyw.demo.mapper;

import lyw.demo.pojo.Contest;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ContestMapper extends Mapper<Contest> {
    @Select("select  c.* from contest c,user_contest uc,status s where uc.uid=#{uid} and uc.cid=c.id and s.cid=c.id and s.display=true")
    List<Contest> selectByUserId(int uid);

    @Delete("delete from user_contest where #{cid}=cid and #{uid}=uid")
    void deleteContestRelation(@Param("cid") int cid, @Param("uid") int uid);

    @Select("select c2.* from contest c2,user_contest uc where c2.id=uc.cid and c2.enddate>now() group by c2.id order by count(c2.id) desc limit 0,3")
    List<Contest> getTopCharacterization();

    @Insert("insert into user_contest(cid,uid) value(#{cid},#{uid})")
    void insertRelation(@Param("uid") int uid, @Param("cid") int cid);

    @Select(value = "select id from user_contest where cid=#{cid} and uid=#{uid}")
    Integer selectRelation(@Param("uid") int uid,@Param("cid") int cid);
}
