package host.luke.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import host.luke.common.pojo.Consumption;
import host.luke.common.pojo.MultiLedger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MultiLedgerMapper extends BaseMapper<MultiLedger> {

    List<Integer> getMLedgerIdByUserId(Long userId);

    int addLedgerUserBind(Long userId,int mLegerId);

    int deleteLedgerUserBind(Long userId,int mLegerId);

    List<Long> getLedgerAllUser(int ledgerId);

    double getBalance(int ledgerId);

    List<Consumption> getLedgerConsList(int ledgerId);

    int addMultiCons(Long userId,Long consId,int mLedgerId);

    int deleteMultiCons(Long userId,Long consId,int mLedgerId);

}
