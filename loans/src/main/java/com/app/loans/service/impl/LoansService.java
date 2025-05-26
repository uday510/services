package com.app.loans.service.impl;

import com.app.loans.constants.LoansConstants;
import com.app.loans.dto.LoansDto;
import com.app.loans.entity.Loans;
import com.app.loans.exception.LoanAlreadyExistsException;
import com.app.loans.exception.ResourceNotFoundException;
import com.app.loans.mapper.LoansMapper;
import com.app.loans.repository.LoansRepository;
import com.app.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class LoansService implements ILoansService {

    private LoansRepository loansRepository;

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> optionalLoans = loansRepository.findByMobileNumber(mobileNumber);
        if (optionalLoans.isPresent()) {
            throw new LoanAlreadyExistsException("Loan Already Exists");
        }
        loansRepository.save(createNewLoan(mobileNumber));
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new loan details
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );

        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    /**
     *
     * @param loansDto - LoansDto Object
     */
    @Override
    public void updateLoad(LoansDto loansDto) {
        Loans loans = loansRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "LoanNumber", loansDto.getLoanNumber()));

        LoansMapper.mapToLoans(loansDto, loans);
        loansRepository.save(loans);
    }

    @Override
    public void deleteLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        loansRepository.deleteById(loans.getLoanId());
    }

    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long newLoanNumber = generate12DigitLoanNumber();
        newLoan.setLoanNumber(Long.toString(newLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);

        return newLoan;
    }

    private long generate12DigitLoanNumber() {
        long min = 100_000_000_000L;
        long max = 999_999_999_999L;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

}
