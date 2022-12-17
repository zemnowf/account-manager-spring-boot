package com.example.accountmanagerspringboot;

import com.example.accountmanagerspringboot.entity.Transfer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;


    public BigDecimal getBalance(Long accountId) {
        BigDecimal balance = transferRepository.getBalanceForId(accountId);
        if(balance == null){
            throw new IllegalArgumentException();
        }
        return balance;
    }

    public BigDecimal replenishBalance(Long to, BigDecimal amount) {
        BigDecimal currentBalance = transferRepository.getBalanceForId(to);
        if (currentBalance == null){
            transferRepository.save(to, amount);
            return amount;
        } else {
            final BigDecimal updatedBalance = currentBalance.add(amount);
            transferRepository.save(to, updatedBalance);
            return updatedBalance;
        }
    }


    public void transfer(Transfer transfer) {
        BigDecimal fromBalance = transferRepository.getBalanceForId(transfer.getFrom());
        BigDecimal toBalance = transferRepository.getBalanceForId(transfer.getTo());
        if (fromBalance == null || toBalance == null){
            throw new IllegalArgumentException("no such account");
        }
        if (transfer.getAmount().compareTo(fromBalance) > 0){
            throw new IllegalArgumentException("not enough");
        }

        BigDecimal updatedFromBalance = fromBalance.subtract(transfer.getAmount());
        BigDecimal updatedToBalance = toBalance.add(transfer.getAmount());

        transferRepository.save(transfer.getFrom(), updatedFromBalance);
        transferRepository.save(transfer.getTo(), updatedToBalance);
    }


}
