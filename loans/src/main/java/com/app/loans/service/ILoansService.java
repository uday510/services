package com.app.loans.service;

import com.app.loans.dto.LoansDto;

public interface ILoansService {

    /**
     *
     * @param mobileNumber - Mobile Number of the Customer
     */
    void createLoan(String  mobileNumber);

    /**
     *
     * @param mobileNumber - Mobile Number of the Customer
     * @return - LoansDto
     */
    LoansDto fetchLoan(String mobileNumber);

    /**
     *
     * @param loansDto - LoansDto Object
     */
    void updateLoad(LoansDto loansDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     */
    void deleteLoan(String mobileNumber);
}
