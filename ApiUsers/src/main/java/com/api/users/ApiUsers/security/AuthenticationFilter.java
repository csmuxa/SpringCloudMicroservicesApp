package com.api.users.ApiUsers.security;

import com.api.users.ApiUsers.model.LoginRequestModel;
import com.api.users.ApiUsers.service.UserService;
import com.api.users.ApiUsers.shared.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;

    private Environment env;

    @Autowired
    public AuthenticationFilter(UserService userService,Environment env){
        this.userService=userService;
        this.env=env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try{
            LoginRequestModel creds=new ObjectMapper().readValue(request.getInputStream(),LoginRequestModel.class);

            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(creds.getEmail(),creds.getPassword(),new
                    ArrayList<>()));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username=((User)authResult.getPrincipal()).getUsername();
        UserDto userDto=userService.getUserDetailsByEmail(username);

        String token= Jwts.builder().setSubject(userDto.getUserId()).setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(env.getProperty("token.expiration_time")))).signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();

              response.addHeader("token",token);
              response.addHeader("userId",userDto.getUserId());

    }
}
