package com.placement_prep_platform.placement_prep_platform.service;

import com.placement_prep_platform.placement_prep_platform.dto.AnalyticsDTO;
import com.placement_prep_platform.placement_prep_platform.dto.DashboardDTO;
import com.placement_prep_platform.placement_prep_platform.model.Resume;
import com.placement_prep_platform.placement_prep_platform.model.TestResult;
import com.placement_prep_platform.placement_prep_platform.model.User;
import com.placement_prep_platform.placement_prep_platform.repository.ResumeRepository;
import com.placement_prep_platform.placement_prep_platform.repository.Role;
import com.placement_prep_platform.placement_prep_platform.repository.UserRepository;
import com.placement_prep_platform.placement_prep_platform.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ResumeRepository resumeRepository;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       ResumeRepository resumeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.resumeRepository = resumeRepository;
    }
    public void registerUser(User user) {
        User newUser=new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Role.Student);
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email Already Exists");
        }
        userRepository.save(newUser);
    }
        public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
        }
    public Optional<User> findUser(User user) {
        Optional<User> loginUser=userRepository.findByEmail(user.getEmail());
        if(loginUser.isPresent() && passwordEncoder.matches(user.getPassword(),loginUser.get().getPassword())){
            return loginUser;
        }
        return null;
    }

    public String loginUser(String email, String password){
        User user=userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));
     if(!passwordEncoder.matches(password,user.getPassword())){
      throw new RuntimeException("Invalid Credentials");
     }
     return jwtUtil.generateToken(user.getEmail());

    }

    public DashboardDTO getDashboard(String email){
        User user =userRepository.findByEmail(email).orElseThrow();
        List<Resume> resumes=resumeRepository.findByUserOrderByUploadDateDesc(user);


        DashboardDTO dto=new DashboardDTO();
        dto.setTotalResumes(resumes.size());
        for (Resume resume : resumes) {

            if (resume.getAtsScore() != null) {

                dto.setLatestScore(
                        resume.getAtsScore()
                );

                dto.setLatestFeedback(
                        resume.getLatestFeedback()
                );

                dto.setLatestMissingSkills(
                        resume.getMissingSkills()
                );

                break;
            }
        }
        return dto;
    }

    public AnalyticsDTO getAnalytics(String email){
        User user=userRepository.findByEmail(email).orElseThrow();

        AnalyticsDTO dto=new AnalyticsDTO();

        List<TestResult> results=user.getTestResults();

        dto.setTotalTests(results.size());

        int highest=0;
        int sum=0;

        for(TestResult t:results){
            highest=Math.max(highest,t.getScore());
            sum+=t.getScore();
        }
        dto.setHighestScore(highest);

        if(!results.isEmpty()){
            dto.setAverageScore(
                    (double) sum/results.size()
            );

            dto.setLatestScore(
                    results.get(results.size()-1).getScore()
            );
        }

return dto;


    }
}
