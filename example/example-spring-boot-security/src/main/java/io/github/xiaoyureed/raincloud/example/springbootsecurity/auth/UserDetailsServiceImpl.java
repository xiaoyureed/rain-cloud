package io.github.xiaoyureed.raincloud.example.springbootsecurity.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.xiaoyureed.raincloud.core.common.exception.SystemException;
import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import io.github.xiaoyureed.raincloud.example.springbootsecurity.account.Account;
import io.github.xiaoyureed.raincloud.example.springbootsecurity.account.AccountService;

/**
 * xiaoyureed@gmail.com
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account one = accountService.lambdaQuery()
            .select(Account::getName, Account::getId, Account::getPassword)
            .eq(Account::getName, username)
            .one();
        System.out.println(one);

        if (one == null) {
            throw new UsernameNotFoundException("user not found, name: " + username);
        }

        return new LoginUserDetails().setUsername(one.getName()).setPassword(one.getPassword()).setId(one.getId());
    }
}
