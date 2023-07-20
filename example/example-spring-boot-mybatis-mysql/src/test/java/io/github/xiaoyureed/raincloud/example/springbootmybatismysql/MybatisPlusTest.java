package io.github.xiaoyureed.raincloud.example.springbootmybatismysql;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.xiaoyureed.raincloud.core.common.model.LoginUserContext;
import io.github.xiaoyureed.raincloud.core.common.model.LoginUserInfo;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.entity.Account;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.mapper.AccountMapper;
import io.github.xiaoyureed.raincloud.example.springbootmybatismysql.service.AccountService;
import jakarta.annotation.Resource;
import net.datafaker.Faker;

/**
 * xiaoyureed@gmail.com
 */
@SpringBootTest
public class MybatisPlusTest {
    @Resource
    AccountMapper accountMapper;

    @Resource
    AccountService accountService;

    @Test
    void test() {
        // 设置登录用户
        LoginUserContext.set(new LoginUserInfo<Object>() {
            @Override
            public Object user() {
                return null;
            }

            @Override
            public String identifier() {
                return "ssssiesee";
            }
        });

        Faker faker = new Faker(Locale.SIMPLIFIED_CHINESE);
        int count = 3;
        List<Account> accounts = IntStream.range(0, count)
            .mapToObj(i -> new Account()
                .setName(faker.name().fullName())
                .setAge(faker.number().numberBetween(1, 150))
                .setPhone(faker.phoneNumber().phoneNumberNational().replace(" ", ""))
                .setBirthday(faker.date().birthday().toLocalDateTime())
            )
            .collect(Collectors.toList());

        accountService.saveBatch(accounts);
//        accountMapper.insertBatch(accounts);
    }

}
