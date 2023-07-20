package io.github.xiaoyureed.raincloud.example.springbootmybatismysql.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import io.github.xiaoyureed.raincloud.core.starter.database.x.AbstractBaseController;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.entity.Account;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.mapper.AccountMapper;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * xiaoyureed@gmail.com
 */
@RestController
@RequestMapping("accounts")
@Tag(name = "account controller")
public class AccountController extends AbstractBaseController<AccountService, AccountMapper, Account> {



}
