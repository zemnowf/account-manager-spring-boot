package com.example.accountmanagerspringboot;

import com.example.accountmanagerspringboot.entity.Transfer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController("/account")
@AllArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @GetMapping("/{accountId}")
    public BigDecimal getBalance(@PathVariable Long accountId){
        return transferService.getBalance(accountId);
    }

    @PostMapping("/replenish")
    public BigDecimal replenishBalance(@RequestBody Transfer transfer){
        return transferService.replenishBalance(transfer.getTo(), transfer.getAmount());
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody Transfer transfer){
        transferService.transfer(transfer);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handle(IllegalArgumentException e){
        log.error(e.getMessage());
        return "Error";
    }
}
