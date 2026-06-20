package com.placement_prep_platform.placement_prep_platform.service;

import com.placement_prep_platform.placement_prep_platform.dto.*;
import com.placement_prep_platform.placement_prep_platform.model.InterviewAnswer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class GeminiService {


    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String callGemini(String prompt) {

        try {

            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                            + apiKey;

            GeminiRequest.Part part = new GeminiRequest.Part();
            part.setText(prompt);

            GeminiRequest.Content content = new GeminiRequest.Content();
            content.setParts(List.of(part));

            GeminiRequest request = new GeminiRequest();
            request.setContents(List.of(content));

            GeminiResponse response =
                    restTemplate.postForObject(
                            url,
                            request,
                            GeminiResponse.class
                    );

            if (response == null
                    || response.getCandidates() == null
                    || response.getCandidates().isEmpty()
                    || response.getCandidates().get(0).getContent() == null
                    || response.getCandidates().get(0).getContent().getParts() == null
                    || response.getCandidates().get(0).getContent().getParts().isEmpty()) {

                return "No response generated";
            }

            return response.getCandidates()
                    .get(0)
                    .getContent()
                    .getParts()
                    .get(0)
                    .getText();

        } catch (Exception e) {

            System.out.println("Gemini Error: " + e.getMessage());

            return "Gemini service unavailable. Please try again later.";
        }
    }

    public ResumeAnalysisResponse analyzeResume(String resumeText) {

        if (resumeText.length() > 5000) {
            resumeText = resumeText.substring(0, 5000);
        }

        String prompt = """
            Analyze this resume and return ONLY in this format:

            ATS_SCORE: <number>

            FEEDBACK:
            <feedback>

            MISSING_SKILLS:
            <comma separated skills>

            Resume:
            """ + resumeText;

        String result = callGemini(prompt);

        int score = 50;
        String feedback = "No feedback generated";
        String missingSkills = "None";

        try {
            String scorePart =
                    result.split("ATS_SCORE:")[1]
                            .split("FEEDBACK:")[0]
                            .trim();

            score = Integer.parseInt(scorePart);

        } catch (Exception ignored) {
        }

        try {
            feedback =
                    result.split("FEEDBACK:")[1]
                            .split("MISSING_SKILLS:")[0]
                            .trim();

        } catch (Exception ignored) {
        }

        try {
            missingSkills =
                    result.split("MISSING_SKILLS:")[1]
                            .trim();

        } catch (Exception ignored) {
        }

        ResumeAnalysisResponse response = new ResumeAnalysisResponse();
        response.setAtsScore(score);
        response.setFeedback(feedback);
        response.setMissingSkills(missingSkills);

        return response;
    }

    public List<String> generateQuestions(String resumeText) {

        String prompt = """
            Based on this resume generate EXACTLY 10 interview questions.

            Mix:
            - Technical Questions
            - Project Based Questions
            - HR Questions

            IMPORTANT:
            Return EXACTLY 10 questions.
            One question per line.
            No numbering.
            No explanations.

            Resume:
            """ + resumeText;

        String result = callGemini(prompt);

        if (result.startsWith("Gemini service unavailable")) {
            return List.of("Unable to generate questions currently.");
        }

        return Arrays.stream(result.split("\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .limit(10)
                .toList();
    }

    public InterviewEvaluationResponse evaluateAnswer(
            String question,
            String answer
    ) {

        String prompt = """
            Evaluate this interview answer.

            QUESTION:
            %s

            ANSWER:
            %s

            Return ONLY in this format:

            SCORE: <0-10>

            FEEDBACK:
            <feedback>
            """.formatted(question, answer);

        String result = callGemini(prompt);

        int score = 5;
        String feedback = "No feedback generated";

        try {

            String scorePart =
                    result.split("SCORE:")[1]
                            .split("FEEDBACK:")[0]
                            .trim();

            score = Integer.parseInt(scorePart);

        } catch (Exception ignored) {
        }

        try {

            feedback =
                    result.split("FEEDBACK:")[1]
                            .trim();

        } catch (Exception ignored) {
        }

        InterviewEvaluationResponse evaluation =
                new InterviewEvaluationResponse();

        evaluation.setScore(score);
        evaluation.setFeedback(feedback);

        return evaluation;
    }

    public String generateOverallInterviewFeedback(
            List<InterviewAnswer> answers
    ) {

        StringBuilder interviewData = new StringBuilder();

        for (InterviewAnswer answer : answers) {

            interviewData.append("""
                Question:
                %s

                Answer:
                %s

                Score:
                %d

                """.formatted(
                    answer.getQuestion(),
                    answer.getAnswer(),
                    answer.getScore()
            ));
        }

        String prompt = """
            Based on these interview results:

            %s

            Give:

            1. Strengths
            2. Weaknesses
            3. Topics to Improve
            4. Overall Interview Readiness (0-100)
            5. Final Recommendation

            Return a detailed report.
            """.formatted(interviewData);

        return callGemini(prompt);
    }

    public String evaluateFullInterview(
            List<String> questions,
            List<String> answers
    ) {

        StringBuilder interviewData = new StringBuilder();

        for (int i = 0; i < questions.size(); i++) {

            interviewData.append("""
                Question:
                %s

                Answer:
                %s

                ---------------------------------

                """.formatted(
                    questions.get(i),
                    answers.get(i)
            ));
        }

        String prompt = """
            Evaluate this complete mock interview.

            %s

            For EACH question provide:

            Question Number
            Score (0-10)
            Feedback

            Then provide:

            OVERALL SCORE (0-100)

            STRENGTHS

            WEAKNESSES

            TOPICS TO IMPROVE

            INTERVIEW READINESS

            Give detailed feedback.
            """.formatted(interviewData);

        return callGemini(prompt);
    }

    public String testGemini() {
        return callGemini("Say Hello");
    }


}
