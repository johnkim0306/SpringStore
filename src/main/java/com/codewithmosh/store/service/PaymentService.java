package com.codewithmosh.store.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {
    private final ConcurrentHashMap<Long, BigDecimal> userBalances = new ConcurrentHashMap<>();
    private final Map<Long, Object> userLocks = new ConcurrentHashMap<>();

    public boolean processPayment(Long userId, BigDecimal amount) {
        userLocks.putIfAbsent(userId, new Object());

        synchronized (userLocks.get(userId)) {
            BigDecimal balance = userBalances.get(userId);

            if (balance == null || balance.compareTo(amount) < 0) {
                return false;
            }

            userBalances.put(userId, balance.subtract(amount));
            return true;
        }
    }

    public void addUser(Long userId, BigDecimal initialBalance) {
        userBalances.putIfAbsent(userId, initialBalance);
    }
}
