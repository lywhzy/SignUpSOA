package lyw.demo.pojo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class MyUserDetails extends User implements UserDetails {
    public MyUserDetails(User user) {
        if (user != null){
            this.setId(user.getId());
            this.setUsername(user.getUsername());
            this.setPhonenumber(user.getPhonenumber());
            this.setPassword(user.getPassword());
            this.setEmail(user.getEmail());
            this.setLevel(user.getLevel());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String username = this.getUsername();
        if (username != null){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.getLevel());
            ((ArrayList<GrantedAuthority>) authorities).add(authority);
        }
        return authorities;
    }



    /**
     * 账户是否未过期，过期无法验证
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁，锁定的用户无法进行身份验证
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据（密码），过期的凭据防止认证
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用，禁用的用户不能身份验证
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
