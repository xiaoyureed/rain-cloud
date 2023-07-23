package io.github.xiaoyureed.raincloud.service.biz.account.account;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xiaoyureed.raincloud.core.starter.mysql.x.AbstractBaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * xiaoyureed@gmail.com
 */
@RestController
@RequestMapping("accounts")
@Tag(name = "account controller")
public class AccountController extends AbstractBaseController<AccountService, AccountMapper, Account> {



}
