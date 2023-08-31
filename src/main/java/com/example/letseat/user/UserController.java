package com.example.letseat.user;

import com.example.letseat.plan.PlanDto;
import com.example.letseat.user.data.ListRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    @GetMapping("/list")
    @ResponseBody
    public List<PlanDto> lists(@RequestBody @Valid ListRequest request) {
        return userService.findPlanDtoByUserId(request.getUser_id());
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> saveMember(@RequestBody @Valid CreateMemberRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setDevice_id(request.getDevice_id());
        Long id = userService.join(user);
        return ResponseEntity.ok("user_id: " + id);
    }
    @Data
    static class CreateMemberRequest{
        private String name;
        private String device_id;

    }
    @Data
    static class CreateMemberResponse{
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
