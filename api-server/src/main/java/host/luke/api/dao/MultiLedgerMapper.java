package host.luke.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import host.luke.common.pojo.Consumption;
import host.luke.common.pojo.MultiLedger;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MultiLedgerMapper extends BaseMapper<MultiLedger> {

    List<Integer> getMLedgerIdByUserId(Long userId);

    int addLedgerUserBind(@Param("userId")Long userId,@Param("mLedgerId")int mLedgerId);

    int deleteLedgerUserBind(@Param("userId")Long userId,@Param("mLedgerId")int mLedgerId);

    List<Long> getLedgerAllUser(int ledgerId);

    double getBalance(int ledgerId);

    List<Consumption> getLedgerConsList(int ledgerId);

    int addMultiCons(@Param("userId")Long userId,@Param("consId")Long consId,@Param("mLedgerId")int mLedgerId);

    int deleteMultiCons(@Param("userId")Long userId,@Param("consId")Long consId,@Param("mLedgerId") int mLedgerId);

}
