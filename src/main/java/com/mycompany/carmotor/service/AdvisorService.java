package com.mycompany.carmotor.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mycompany.carmotor.model.domain.Advisor;
import com.mycompany.carmotor.repository.AdvisorRepository;

@Service
public class AdvisorService {

    private final AdvisorRepository advisorRepository;

    public AdvisorService(AdvisorRepository advisorRepository) {
        this.advisorRepository = advisorRepository;
    }

    public List<Advisor> getAllAdvisors() {
        return advisorRepository.findAll();
    }

    public Advisor getAdvisorById(Long id) {
        return advisorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advisor not found with id: " + id));
    }

    public Advisor saveAdvisor(Advisor advisor) {
        return advisorRepository.save(advisor);
    }

    public void deleteAdvisor(Long id) {
        advisorRepository.deleteById(id);
    }
}