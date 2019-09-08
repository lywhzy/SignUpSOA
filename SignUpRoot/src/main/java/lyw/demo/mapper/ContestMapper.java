package lyw.demo.mapper;

import lyw.demo.pojo.Contest;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ContestMapper extends Mapper<Contest> {
    @Select("select * from contest,status where contest.id=status.cid and status.checkstatus=0")
    List<Contest> findNotAudit();

    @Select("select status.display from status where status.id=#{cid}")
    Boolean selectDisplayByCid(int cid);

    @Update("update status set status.checkstatus=1,status.display=1 where status.cid=#{cid}")
    void updateCheckStatusByCid(@Param("cid") int cid);

    @Update("update status set status.display=#{dis} where status.cid=#{cid}")
    void updateDisplayByCid(@Param("cid") int cid, @Param("dis") Boolean display);

}
