package com.grepiu.www.process.common.config;

import com.grepiu.www.process.common.config.auth.domain.Role;
import com.grepiu.www.process.common.config.auth.service.MongoClientDetailsService;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 *
 * 인증 서버 설정
 * TODO: 2018-07-13   구현중
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  static final String CLIEN_ID = "grepiu-client";
  static final String CLIENT_SECRET = "grepiu-secret";
  static final String GRANT_TYPE_PASSWORD = "password";
  static final String AUTHORIZATION_CODE = "authorization_code";
  static final String REFRESH_TOKEN = "refresh_token";
  static final String IMPLICIT = "implicit";
  static final String SCOPE_READ = "read";
  static final String SCOPE_WRITE = "write";
  static final String TRUST = "trust";
  static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1*60*60;
  static final int FREFRESH_TOKEN_VALIDITY_SECONDS = 6*60*60;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JedisConnectionFactory jedisConnectionFactory;

  @Autowired
  private UserDetailsService currentUserDetailService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    oauthServer.tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()");
  }

  /**
   *
   * API 요청 클라이언트 정보를 다룬다.
   *
   * @param clients
   * @throws Exception
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
/*//    clients.withClientDetails(mongoClientDetailsService);*/
    clients.inMemory()
        .withClient(CLIEN_ID)
        .authorizedGrantTypes("password", "client_credentials", "authorization_code", "refresh_token")
        .authorities(Role.USER.toString())
        .scopes(SCOPE_READ, SCOPE_WRITE)
//        .resourceIds("grepiu")
        .secret(new BCryptPasswordEncoder().encode(CLIENT_SECRET));
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.tokenStore(tokenStore())
        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
        .authenticationManager(authenticationManager)
        .userDetailsService(currentUserDetailService);
  }

  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }

}
