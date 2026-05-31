package com.mycompany.carmotor.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mycompany.carmotor.model.domain.BankEntity;
import com.mycompany.carmotor.repository.BankEntityRepository;

@Service
public class BankEntityService {

    private final BankEntityRepository bankEntityRepository;

    public BankEntityService(BankEntityRepository bankEntityRepository) {
        this.bankEntityRepository = bankEntityRepository;
    }

    public List<BankEntity> getAllBankEntities() {
        return bankEntityRepository.findAll();
    }

    public BankEntity getBankEntityById(Long id) {
        return bankEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank entity not found with id: " + id));
    }

    public BankEntity saveBankEntity(BankEntity bankEntity) {
        return bankEntityRepository.save(bankEntity);
    }

    public void deleteBankEntity(Long id) {
        bankEntityRepository.deleteById(id);
    }
}