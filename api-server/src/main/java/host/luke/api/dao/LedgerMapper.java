package host.luke.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import host.luke.common.pojo.Ledger;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LedgerMapper extends BaseMapper<Ledger> {
}
