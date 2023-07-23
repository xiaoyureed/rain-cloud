package io.github.xiaoyureed.raincloud.example.springbootmybatismysql.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xiaoyureed.raincloud.core.starter.mysql.x.AbstractBaseController;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.entity.Account;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.mapper.AccountMapper;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * xiaoyureed@gmail.com
 */
@RestController
@RequestMapping("accounts")
@Tag(name = "account controller")
public class AccountController extends AbstractBaseController<AccountService, AccountMapper, Account> {



}
