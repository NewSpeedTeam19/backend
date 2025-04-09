package com.newspeed19.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newspeed19.auth.service.JwtProvider;
import com.newspeed19.filter.AuthFilter;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

/**
 * @packageName    : com.newspeed19.config
 * @fileName       : WebConfig
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig {
	private final JwtProvider jwtProvider;

	@Bean
	public FilterRegistrationBean authFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new AuthFilter(jwtProvider));
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}

}
