package io.github.xiaoyureed.raincloud.core.starter.security.auth.sms;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import io.github.xiaoyureed.raincloud.core.starter.security.auth.phone.PhoneUserDetails;

/**
 * xiaoyureed@gmail.com
 */
//@Component
public class SmsUserDetailsServiceImpl implements UserDetailsService {
    private String phoneDB = "17521090691";

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        if (StringUtils.equals(phoneDB, phone)) {
            return new PhoneUserDetails().setPhone(phoneDB);
        }

        throw new UsernameNotFoundException("phone not found");
    }
}
