package org.example.service.impl;

import org.example.service.EnterService;

public class EnterServiceImpl implements EnterService {
    @Override
    public boolean enter(String username, String password) {
        if (username.isEmpty() || password.isEmpty())
            return false;
        else {
            if (username.equals("userok") && password.equals("13ab7654"))
                return true;
            else
                return false;
        }
    }
}
