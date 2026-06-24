package com.placement_prep_platform.placement_prep_platform.service;

import com.placement_prep_platform.placement_prep_platform.model.DashboardResponse;
import com.placement_prep_platform.placement_prep_platform.model.InterviewAnswer;
import com.placement_prep_platform.placement_prep_platform.model.Resume;
import com.placement_prep_platform.placement_prep_platform.model.User;
import com.placement_prep_platform.placement_prep_platform.repository.InterviewAnswerRepository;
import com.placement_prep_platform.placement_prep_platform.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ResumeRepository resumeRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;

    public DashboardResponse getDashboard(User user) {

        List<Resume> resumes = resumeRepository.findByUser(user);

        long totalResumes = resumes.size();
Resume latestResume = resumes.stream().max(Comparator.comparing(Resume::getUploadDate))
        .orElse(null);
        Integer latestAts = latestResume != null ? latestResume.getAtsScore():0;

        Double avgAts = resumes.stream()
                .mapToInt(Resume::getAtsScore)
                .average()
                .orElse(0);

        List<InterviewAnswer> answers =
                interviewAnswerRepository.findByUser(user);

        long interviewsTaken = answers.size()/10;

        Integer highestScore = answers.stream()
                .mapToInt(InterviewAnswer::getScore)
                .max()
                .orElse(0);
        answers.forEach(answer ->
                System.out.println("Score = " + answer.getScore()));

        Double avgInterview = answers.stream()
                .mapToInt(InterviewAnswer::getScore)
                .average()
                .orElse(0);

        int readiness =
                (int) ((avgAts + (avgInterview * 10)) / 2);
        List<Integer> atsScore=resumes.stream()
                .map(Resume::getAtsScore)
                .toList();
        List<Integer> interviewScores=answers.stream()
                .map(InterviewAnswer::getScore)
                .toList();

        return new DashboardResponse(
                totalResumes,
                latestAts,
                avgAts,
                interviewsTaken,
                highestScore,
                avgInterview,
                readiness,
                atsScore,
                interviewScores
        );
    }

}
