<<<<<<< HEAD
# AI-Powered-Placement-Preparation-Platform
AI-powered placement preparation platform with resume analysis, mock interviews, coding practice, aptitude tests, and AI feedback.
=======
# AI-Powered Placement Preparation Platform

The Placement Preparation Platform is a full-stack web application designed to help students improve their job readiness by analyzing resumes, identifying skill gaps, generating interview questions, and tracking learning progress.

## Core Functionality

### 1. Resume Analysis

Users upload their resumes in PDF format along with a target job description. The system extracts resume content and sends both the resume and job description to an AI model for analysis.

The AI evaluates:

* ATS (Applicant Tracking System) score
* Resume strengths
* Resume weaknesses
* Missing skills compared to the job description
* Suggestions for improvement

The analysis results are stored in the database for future reference.

### 2. Skill Gap Detection

Based on the AI analysis, the system identifies skills that are required by the job description but missing from the user's resume.

Examples:

* AWS
* Docker
* Spring Security
* SQL Optimization

These skills are automatically saved in the Skill Gap Tracker.

### 3. Skill Gap Tracker

The Skill Gap Tracker helps users monitor their learning progress.

Features include:

* Viewing all missing skills
* Marking skills as completed
* Tracking completed vs pending skills
* Storing progress permanently in the database

This transforms AI recommendations into an actionable learning roadmap.

### 4. Interview Question Generation

The platform generates personalized interview questions using AI based on:

* Resume content
* User skills
* Target job description

Questions are generated once and stored in the database to avoid unnecessary AI API calls.

### 5. Interview Evaluation

Users can submit answers to generated interview questions.

The AI evaluates:

* Technical correctness
* Communication quality
* Areas for improvement

This provides realistic interview practice and feedback.

### 6. Dashboard & Analytics

The dashboard provides a centralized view of the user's progress, including:

* ATS score
* Number of generated interview questions
* Skill completion progress
* Resume analysis history

## Technology Stack

### Backend

* Java
* Spring Boot
* Spring MVC
* Spring Security
* Spring Data JPA
* Hibernate

### Frontend

* Thymeleaf
* Bootstrap 5
* HTML/CSS
* JavaScript

### Database

* MySQL

### AI Integration

* Gemini API (Google Generative AI)

### Authentication

* JWT-based Authentication
* Role-based access control

## Workflow

1. User uploads resume.
2. AI analyzes resume against job description.
3. ATS score and missing skills are generated.
4. Missing skills are saved in Skill Gap Tracker.
5. AI generates interview questions.
6. User practices interviews.
7. User marks completed skills.
8. Progress is tracked and displayed on the dashboard.

## Key Benefits

* Personalized career guidance
* ATS optimization
* Skill gap identification
* Interview preparation
* Progress tracking
* AI-driven recommendations

The platform acts as an intelligent career preparation assistant, helping students understand where they stand, what skills they need to learn, and how to prepare effectively for placements and internships.
>>>>>>> 79bd57f10c51ea0a3dd0e728803604cde07cadf5


## AI FLOW
Controller

↓

Service

↓

GeminiService

↓

Prompt

↓

Gemini API

↓

Response

↓

Parse Response

↓

Return Java Object

↓

Save Database

↓

Display HTML


## MY prOJeCt STRUCTURE

Browser

↓

HTML (Thymeleaf)

↓

Controller

↓

Service

↓

Repository

↓

Database

↓

Service

↓

Controller

↓

HTML

↓

Browser

## Overall Workflow
Login
   │
Dashboard
   │
Upload Resume
   │
Extract PDF Text
   │
Analyze Resume (Gemini)
   │
Save ATS Score
   │
Generate Questions (Gemini)
   │
Save Questions
   │
Take Mock Interview
   │
Submit Answers
   │
Evaluate Interview (Gemini)
   │
Show Report
   │
Update Analytics

## CONCEPTS USED
Spring Boot MVC (Controllers, Services, Repositories)
Spring Data JPA
MySQL
JWT Authentication
File Upload
PDF Text Extraction
External API Integration (Gemini)
Thymeleaf
Chart.js
Basic Analytics
Entity Relationships
