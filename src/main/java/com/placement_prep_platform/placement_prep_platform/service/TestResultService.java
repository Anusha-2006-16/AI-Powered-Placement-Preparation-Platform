package com.placement_prep_platform.placement_prep_platform.service;

import com.placement_prep_platform.placement_prep_platform.model.TestResult;
import com.placement_prep_platform.placement_prep_platform.model.User;
import com.placement_prep_platform.placement_prep_platform.repository.TestResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestResultService {
    private final TestResultRepository testResultRepository;
    public TestResultService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }
    public void saveTestResult(TestResult testResult) {
        testResultRepository.save(testResult);
    }

    public void saveResult(TestResult testResult) {
        testResultRepository.save(testResult);
    }
    public List<TestResult> getResultsByUser(User user){
        return testResultRepository.findByUser(user);
    }
}
