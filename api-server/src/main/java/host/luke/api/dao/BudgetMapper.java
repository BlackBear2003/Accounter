package host.luke.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import host.luke.common.pojo.Budget;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BudgetMapper extends BaseMapper<Budget> {

    public List<Integer> getAllBudgetIdByUserId(Long userId);

    public Integer addBudgetForUser(@Param("bid") Integer budgetId, @Param("uid") Long userId);

    public Integer dropBudgetForUser(@Param("bid") Integer budgetId, @Param("uid") Long userId);
}
