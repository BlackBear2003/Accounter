package host.luke.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import host.luke.api.dao.LedgerMapper;
import host.luke.common.pojo.Ledger;
import org.springframework.stereotype.Service;

@Service
public class LedgerServiceImpl extends ServiceImpl<LedgerMapper, Ledger> {
}
