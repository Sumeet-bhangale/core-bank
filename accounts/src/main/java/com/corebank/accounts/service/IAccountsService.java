package com.corebank.accounts.service;

import com.corebank.accounts.dto.CustomerDto;

public interface IAccountsService {

        void createAccount(CustomerDto customerDto);

        //Input Mobile Number
        //Return Account Details based on given mobile number
        CustomerDto fetchAccount(String mobileNumber);

        boolean updateAccount(CustomerDto customerDto);

        boolean deleteAccount(String mobileNumber);

}
