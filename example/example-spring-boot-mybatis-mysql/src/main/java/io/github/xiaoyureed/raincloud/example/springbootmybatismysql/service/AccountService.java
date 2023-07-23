package io.github.xiaoyureed.raincloud.example.springbootmybatismysql.service;

import org.springframework.stereotype.Service;

import io.github.xiaoyureed.raincloud.core.starter.mysql.x.AbstractBaseServiceX;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.entity.Account;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.mapper.AccountMapper;

/**
 * xiaoyureed@gmail.com
 */
@Service
public class AccountService extends AbstractBaseServiceX<AccountMapper, Account> {
}
