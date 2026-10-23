package com.example.cognit_invest.services.finance;

import com.example.cognit_invest.DTOs.finance.bankAccount.BankAccountRequestDTO;
import com.example.cognit_invest.DTOs.finance.bankAccount.BankAccountResponseDTO;
import com.example.cognit_invest.components.SessionUser;
import com.example.cognit_invest.exceptions.user.UserNotFoundException;
import com.example.cognit_invest.models.finance.BankAccount;
import com.example.cognit_invest.models.people.User;
import com.example.cognit_invest.repositories.auth.UserRepository;
import com.example.cognit_invest.repositories.finance.BankAccountRepository;
import com.example.cognit_invest.services.internal.Logger;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class BankAccountService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BankAccountResponseDTO saveAccount(
            BankAccountRequestDTO dto,
            SessionUser sessionUser
    ){
        Logger.debug("Nome: "+sessionUser.name());
        User user = userRepository.findById(sessionUser.id()).orElseThrow(UserNotFoundException::new);
        Logger.debug("usuário encontrado!");

        BankAccount bankAccount = new BankAccount(
                user.getId(),
                dto.name(),
                dto.bank(),
                dto.balance(),
                dto.useCredit(),
                dto.creditLimit(),
                dto.usedCredit(),
                dto.invoiceClosing(),
                dto.invoiceDueDate()
        );

        user.setTotalBalance(
                user.getTotalBalance().add(dto.balance())
        );
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        bankAccount = bankAccountRepository.save(bankAccount);
        return BankAccountResponseDTO.fromEntity(bankAccount);
    }

    public List<BankAccountResponseDTO> getAllAccounts(
            SessionUser sessionUser
    ){
        if(!userRepository.existsById(sessionUser.id()))throw new UserNotFoundException();

        return bankAccountRepository.findAllByUserIdAndDeletedAtIsNull(sessionUser.id()).stream()
                .map(BankAccountResponseDTO::fromEntity)
                .toList();
    }
}
