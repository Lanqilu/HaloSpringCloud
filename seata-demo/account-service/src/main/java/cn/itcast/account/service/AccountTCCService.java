package cn.itcast.account.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @author Halo
 * @create 2021/09/15 下午 07:16
 * @description
 */
@LocalTCC
public interface AccountTCCService {
    /**
     * 从用户账户中扣款
     */
    @TwoPhaseBusinessAction(name = "deduct", commitMethod = "confirm", rollbackMethod = "cancel")
    void deduct(@BusinessActionContextParameter(paramName = "userId") String userId,
                @BusinessActionContextParameter(paramName = "money") int money);
    boolean confirm (BusinessActionContext context);
    boolean cancel (BusinessActionContext context);
}
