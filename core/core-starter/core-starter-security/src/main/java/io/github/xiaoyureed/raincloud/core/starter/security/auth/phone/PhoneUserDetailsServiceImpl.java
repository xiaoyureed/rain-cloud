package io.github.xiaoyureed.raincloud.core.starter.security.auth.phone;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import io.github.xiaoyureed.raincloud.core.common.model.LoginUserInfo;

/**
 * xiaoyureed@gmail.com
 */
//@Component
public class PhoneUserDetailsServiceImpl implements UserDetailsService {

    private String phoneDb = "17521090691";

    /**
     * 根据手机号查询
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        if (phoneDb.contentEquals(phone)) {
            return new PhoneUserDetails().setPhone(phone);
        }

        throw new UsernameNotFoundException("phone number not found");
    }
}
