package cn.byteboy.activitiplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
@Configuration
public class Config {

    @Bean
    public UserDetailsService myUserDetailsService(){
//        把用户存储在内存中
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        构造用户的信息
        String[][] usersGroupAndRoles= {
                {"root","password","ROLE_ACTIVITI_USER","GROUP_activitiTeam"},
        };

        for (String[] users : usersGroupAndRoles) {
//            用户的角色和组
            List<String> authStrList = Arrays.asList(Arrays.copyOfRange(users, 2, users.length));


            inMemoryUserDetailsManager.createUser(new User(users[0],
                    passwordEncoder().encode(users[1]),
                    authStrList.stream().map(str -> new SimpleGrantedAuthority(str)).collect(Collectors.toList())));
        }
        return inMemoryUserDetailsManager;

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
