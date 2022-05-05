package com.example.orders.serviceImpls;

import com.example.orders.entityes.Other;
import com.example.orders.repositories.OtherRepo;
import com.example.orders.services.OtherService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class OtherServiceImpl implements OtherService {
    private OtherRepo otherRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Other other=otherRepo.findById(username).orElseThrow(()->new UsernameNotFoundException(username+" is not exists"));
        Collection<GrantedAuthority> grantedAuthorities= Stream.of(new SimpleGrantedAuthority(other.getRole())).collect(Collectors.toList());
        return new User(username, other.getPassword(),grantedAuthorities);
    }
}
