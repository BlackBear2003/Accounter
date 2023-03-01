package host.luke.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import host.luke.common.pojo.Ledger;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LedgerMapper extends BaseMapper<Ledger> {

    public Integer addConsForLedger(@Param("lid") Integer ledgerId,@Param("cid") Long ConsumptionId);

    public Integer dropConsForLedger(@Param("lid") Integer ledgerId,@Param("cid")Long ConsumptionId);

    public List<Long> getConsIdByLedgerId(Integer ledgerId);

    public Double getBalance(Integer ledgerId);

}
