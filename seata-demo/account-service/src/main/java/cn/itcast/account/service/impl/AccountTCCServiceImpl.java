package cn.itcast.account.service.impl;

import cn.itcast.account.entity.AccountFreeze;
import cn.itcast.account.mapper.AccountFreezeMapper;
import cn.itcast.account.mapper.AccountMapper;
import cn.itcast.account.service.AccountTCCService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Halo
 * @create 2021/09/15 下午 07:21
 * @description
 */
@Service
@Slf4j
public class AccountTCCServiceImpl implements AccountTCCService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountFreezeMapper freezeMapper;

    @Override
    @Transactional
    public void deduct(String userId, int money) {
        // 获取事务id
        String xid = RootContext.getXID();
        // 判断 freeze 是否有冻结记录
        AccountFreeze oldFreeze = freezeMapper.selectById(xid);
        if (oldFreeze != null) {
            // 已经处理过一次 CANCEL，无需重复处理
            return;
        }
        // 1. 扣减可以余额
        accountMapper.deduct(userId, money);
        // 2. 记录冻结余额，事务状态
        AccountFreeze freeze = new AccountFreeze();
        freeze.setUserId(userId);
        freeze.setFreezeMoney(money);
        freeze.setState(AccountFreeze.State.TRY);
        freeze.setXid(xid);
        freezeMapper.insert(freeze);
    }

    @Override
    public boolean confirm(BusinessActionContext context) {
        // 获取事务id
        String xid = context.getXid();
        // 根据 id 删除冻结记录
        return freezeMapper.deleteById(xid) == 1;
    }

    @Override
    public boolean cancel(BusinessActionContext context) {
        // 查询冻结记录
        String xid = context.getXid();
        AccountFreeze freeze = freezeMapper.selectById(xid);
        // 空回滚判断
        if (freeze == null) {
            // 需要空回滚
            freeze.setUserId(context.getActionContext("userId").toString());
            freeze.setFreezeMoney(0);
            freeze.setState(AccountFreeze.State.CANCEL);
            freeze.setXid(xid);
            freezeMapper.insert(freeze);
            return true;
        }

        // 幂等判断
        if (freeze.getState() == AccountFreeze.State.CANCEL) {
            // 已经处理过了
            return true;
        }

        // 恢复可用余额
        accountMapper.refund(freeze.getUserId(), freeze.getFreezeMoney());
        // 将冻结余额清零
        freeze.setFreezeMoney(0);
        freeze.setState(AccountFreeze.State.CANCEL);
        return freezeMapper.updateById(freeze) == 1;
    }
}
