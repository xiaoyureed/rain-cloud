package io.github.xiaoyureed.raincloud.service.support.authserver.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * xiaoyureed@gmail.com
 */
@RestController
//@RequestMapping("login")
public class LoginController {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    HttpSession session;

    @PostMapping("login")
    public ResponseEntity<ResponseWrapper<?>> login() {
        return null;
    }

    @GetMapping("captcha")
    public void captcha() throws Throwable {
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 5);
        String code = circleCaptcha.getCode();

        // 普通应用可以保存在 session 中, 等待校验
        session.setAttribute("code", code);

        circleCaptcha.write(response.getOutputStream());
    }

}
