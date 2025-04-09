package com.newspeed19.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newspeed19.filter.AuthFilter;

import jakarta.servlet.Filter;

/**
 * @packageName    : com.newspeed19.config
 * @fileName       : WebConfig
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */
@Configuration
public class WebConfig {
	@Value("${jwt.secret}")
	private String secretKey;

	@Bean
	public FilterRegistrationBean authFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new AuthFilter(secretKey));
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}
}
