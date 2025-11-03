package com.stech.quiz.controller;

import com.stech.quiz.entity.QuizAttempt;
import com.stech.quiz.service.QuizAttemptService;
import com.stech.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.stech.quiz.entity.Quiz;
import com.stech.quiz.entity.Question;
import java.util.List;
import com.stech.quiz.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/quiz/attempt")
@RequiredArgsConstructor
public class QuizAttemptController {
    private final QuizAttemptService attemptService;
    private final QuizService quizService;
    private final UserService userService;

    @PostMapping("/start/{quizId}")
    public String startQuiz(@PathVariable Long quizId, Model model, RedirectAttributes ra) {
        Quiz quiz = quizService.getQuizById(quizId);
        // Validation: quiz must be active and have at least 1 question
        if (quiz == null || Boolean.FALSE.equals(quiz.getActive())) {
            ra.addFlashAttribute("error", "Quiz is not active.");
            return "redirect:/dashboard";
        }
        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            ra.addFlashAttribute("error", "Quiz has no questions.");
            return "redirect:/dashboard";
        }
        QuizAttempt attempt = attemptService.startQuiz(
            quiz,
            userService.getCurrentUser()
        );
        return "redirect:/quiz/attempt/" + attempt.getId() + "/q/1";
    }

    @GetMapping("/{attemptId}/q/{index}")
    public String showQuestion(@PathVariable Long attemptId, @PathVariable int index, Model model) {
        QuizAttempt attempt = attemptService.getAttempt(attemptId);
        Quiz quiz = attempt.getQuiz();
        List<Question> questions = quiz.getQuestions();
        if (index < 1) {
            return "redirect:/quiz/attempt/" + attemptId + "/q/1";
        }
        if (index > questions.size()) {
            return "redirect:/quiz/attempt/" + attemptId + "/q/1";
        }
        // Resolve current question by shuffled order
        Long currentQid = attempt.getQuestionOrder() != null && attempt.getQuestionOrder().size() >= index
                ? attempt.getQuestionOrder().get(index - 1)
                : questions.get(index - 1).getId();
        Question current = questions.stream().filter(q -> q.getId().equals(currentQid)).findFirst().orElse(questions.get(index - 1));
        // Prepare deterministically shuffled answers for display (stable across renders)
        java.util.List<com.stech.quiz.entity.Answer> shuffledAnswers = new java.util.ArrayList<>(current.getAnswers());
        shuffledAnswers.sort((a,b) -> {
            int ha = java.util.Objects.hash(attempt.getId(), a.getId());
            int hb = java.util.Objects.hash(attempt.getId(), b.getId());
            return Integer.compare(ha, hb);
        });
        Long selectedAnswerId = attempt.getUserAnswers() != null ? attempt.getUserAnswers().get(current.getId()) : null;
        int answeredCount = attempt.getUserAnswers() != null ? attempt.getUserAnswers().size() : 0;
        model.addAttribute("attempt", attempt);
        model.addAttribute("quiz", quiz);
        model.addAttribute("question", current);
        model.addAttribute("answers", shuffledAnswers);
        model.addAttribute("index", index);
        model.addAttribute("total", questions.size());
        model.addAttribute("answeredCount", answeredCount);
        model.addAttribute("selectedAnswerId", selectedAnswerId);
        return "quiz/attempt-step";
    }

    @PostMapping("/answer/{attemptId}")
    public ResponseEntity<Void> saveAnswerAndNavigate(@PathVariable Long attemptId,
                                                      @RequestParam Long questionId,
                                                      @RequestParam(required = false) Long answerId,
                                                      @RequestParam int index,
                                                      @RequestParam String nav,
                                                      HttpServletRequest request) {
        attemptService.saveAnswer(attemptId, questionId, answerId);
        // For AJAX we simply return 200 OK; client will load the next fragment itself
        return ResponseEntity.ok().build();
    }

    @PostMapping("/submit/{attemptId}")
    public String submit(@PathVariable Long attemptId,
                         @RequestParam Long questionId,
                         @RequestParam(required = false) Long answerId) {
        // Save last answer then submit
        attemptService.saveAnswer(attemptId, questionId, answerId);
        var result = attemptService.finalizeAttempt(attemptId);
        return "redirect:/quiz/result/" + result.getId();
    }

    @GetMapping("/{attemptId}/q/{index}/fragment")
    public String showQuestionFragment(@PathVariable Long attemptId, @PathVariable int index, Model model) {
        // Reuse the same logic as showQuestion to populate model
        QuizAttempt attempt = attemptService.getAttempt(attemptId);
        Quiz quiz = attempt.getQuiz();
        List<Question> questions = quiz.getQuestions();
        if (index < 1) index = 1;
        if (index > questions.size()) index = questions.size();
        Long currentQid = attempt.getQuestionOrder() != null && attempt.getQuestionOrder().size() >= index
                ? attempt.getQuestionOrder().get(index - 1)
                : questions.get(index - 1).getId();
        Question current = questions.stream().filter(q -> q.getId().equals(currentQid)).findFirst().orElse(questions.get(index - 1));
        java.util.List<com.stech.quiz.entity.Answer> shuffledAnswers = new java.util.ArrayList<>(current.getAnswers());
        shuffledAnswers.sort((a,b) -> {
            int ha = java.util.Objects.hash(attempt.getId(), a.getId());
            int hb = java.util.Objects.hash(attempt.getId(), b.getId());
            return Integer.compare(ha, hb);
        });
        Long selectedAnswerId = attempt.getUserAnswers() != null ? attempt.getUserAnswers().get(current.getId()) : null;
        int answeredCount = attempt.getUserAnswers() != null ? attempt.getUserAnswers().size() : 0;
        model.addAttribute("attempt", attempt);
        model.addAttribute("quiz", quiz);
        model.addAttribute("question", current);
        model.addAttribute("answers", shuffledAnswers);
        model.addAttribute("index", index);
        model.addAttribute("total", questions.size());
        model.addAttribute("answeredCount", answeredCount);
        model.addAttribute("selectedAnswerId", selectedAnswerId);
        return "quiz/attempt-step :: questionBody";
    }
}
