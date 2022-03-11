package com.personal.kakaoLogin.oAuthLogin.repository;

import com.personal.kakaoLogin.oAuthLogin.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
public class UserRepository {
    private static final Map<String, User> store = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    public User save(User user){
        user.setId(++sequence);
        store.put(user.getEmail(), user);
        return user;
    }

    public User findByEmail(String email){
        return store.get(email);
    }

    public List<User> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }


}
