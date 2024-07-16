package com.example.spring_start.user.service;

import com.example.spring_start.user.domain.Level;
import com.example.spring_start.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    User user;

    @BeforeEach
    public void setUp(){
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for(Level level : levels){
            if (level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assert(user.getLevel() == level.nextLevel());
        }
    }

    @Test
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for(Level level : levels){
            if (level.nextLevel() != null) continue;
            assertThrows(IllegalStateException.class, () ->
            {
                user.setLevel(level);
                user.upgradeLevel();
            });
        }
    }
}
