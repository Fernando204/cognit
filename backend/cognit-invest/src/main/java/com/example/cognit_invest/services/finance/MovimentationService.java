package com.example.cognit_invest.services.finance;

import com.example.cognit_invest.DTOs.finance.movimentation.ConfirmMovimentationDTO;
import com.example.cognit_invest.DTOs.finance.movimentation.MovimentationRequestDTO;
import com.example.cognit_invest.DTOs.finance.movimentation.MovimentationResponseDTO;
import com.example.cognit_invest.components.SessionUser;
import com.example.cognit_invest.enums.finance.MovimentationType;
import com.example.cognit_invest.enums.finance.PaymentWay;
import com.example.cognit_invest.exceptions.finance.BankAccountNotFoundException;
import com.example.cognit_invest.exceptions.finance.ConfirmedMovimentationException;
import com.example.cognit_invest.exceptions.finance.MovimentationNotFoundException;
import com.example.cognit_invest.exceptions.user.UserNotFoundException;
import com.example.cognit_invest.models.finance.BankAccount;
import com.example.cognit_invest.models.finance.Movimentation;
import com.example.cognit_invest.models.people.User;
import com.example.cognit_invest.repositories.auth.UserRepository;
import com.example.cognit_invest.repositories.finance.BankAccountRepository;
import com.example.cognit_invest.repositories.finance.MovimentationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class MovimentationService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final MovimentationRepository movimentationRepository;

    public MovimentationService(BankAccountRepository bankAccountRepository, UserRepository userRepository, MovimentationRepository movimentationRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.movimentationRepository = movimentationRepository;
    }

    @Transactional
    public MovimentationResponseDTO saveMovimentation(
            MovimentationRequestDTO dto,
            SessionUser sessionUser
    ){
        User user = userRepository.findById(sessionUser.id()).orElseThrow(UserNotFoundException::new);
        BankAccount bankAccount = bankAccountRepository.findById(dto.accountId()).orElseThrow(BankAccountNotFoundException::new);

        Movimentation movimentation = new Movimentation(
                dto.accountId(),
                dto.category(),
                dto.description(),
                dto.type(),
                sessionUser.id(),
                dto.value(),
                dto.paymentWay()
        );

        if(movimentation.getPaymentWay() != PaymentWay.PENDENTE){

            if(movimentation.getType() == MovimentationType.ENTRY){
                bankAccount.addToBalance(movimentation.getValue());
                user.addToBalance(movimentation.getValue());

            } else if (movimentation.getType() == MovimentationType.EXIT) {

                if(movimentation.getPaymentWay() == PaymentWay.CARTAO_DE_CREDITO){
                   bankAccount.consumeCredit(movimentation.getValue());
                }else{
                    bankAccount.removeFromBalance(movimentation.getValue());
                    user.removeFromBalance(movimentation.getValue());
                }
            }

        }

        movimentation = movimentationRepository.save(movimentation);
        userRepository.save(user);
        bankAccountRepository.save(bankAccount);

        return MovimentationResponseDTO.fromEntity(movimentation);
    }

    @Transactional
    public MovimentationResponseDTO confirmPending(
            ConfirmMovimentationDTO dto,
            SessionUser sessionUser
    ){
        User user = userRepository.findById(sessionUser.id()).orElseThrow(UserNotFoundException::new);

        Movimentation movimentation = movimentationRepository.findById(dto.movimentationId()).orElseThrow(MovimentationNotFoundException::new);
        if(movimentation.getPaymentWay() != PaymentWay.PENDENTE) throw new ConfirmedMovimentationException();
        BankAccount bankAccount = bankAccountRepository.findById(movimentation.getAccountId()).orElseThrow(BankAccountNotFoundException::new);

        movimentation.setPaymentWay(dto.paymentWay());
        movimentation.setUpdatedAt(Instant.now());

        if(movimentation.getType() == MovimentationType.ENTRY){
            bankAccount.addToBalance(movimentation.getValue());
            user.addToBalance(movimentation.getValue());

        } else if (movimentation.getType() == MovimentationType.EXIT) {

            if(movimentation.getPaymentWay() == PaymentWay.CARTAO_DE_CREDITO){
                bankAccount.consumeCredit(movimentation.getValue());
            }else{
                bankAccount.removeFromBalance(movimentation.getValue());
                user.removeFromBalance(movimentation.getValue());
            }
        }

        movimentation = movimentationRepository.save(movimentation);
        userRepository.save(user);
        bankAccountRepository.save(bankAccount);

        return MovimentationResponseDTO.fromEntity(movimentation);
    }

    public List<MovimentationResponseDTO> getMovimentations(
            SessionUser sessionUser
    ){
        if(!userRepository.existsById(sessionUser.id())) throw new UserNotFoundException();
        return movimentationRepository.findAllByUserIdAndDeletedAtIsNull(sessionUser.id()).stream()
                .map(MovimentationResponseDTO::fromEntity)
                .toList();
    }
}
