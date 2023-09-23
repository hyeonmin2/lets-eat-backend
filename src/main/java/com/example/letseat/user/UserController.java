package com.example.letseat.user;

import com.example.letseat.auth.AuthMember;
import com.example.letseat.auth.argumentresolver.Auth;
import com.example.letseat.user.data.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<ListResponse>> lists(@Auth AuthMember authMember) {
        System.out.println("authMember = " + authMember);
        if(authMember==null){
            throw  new RuntimeException("authMember가 없음");
        }
        Long user_id = authMember.getId();
        List<ListResponse> planList = userService.findPlanByUserId(user_id);
        return ResponseEntity.ok(planList);
    }

    @PostMapping("/sign-up")
    @ResponseBody // device id 중복 기능 추가해야함.
    public Map<String, Object> saveMember(@RequestBody @Valid SignUpRequest request) {
        User user = new User();
        user.setName(request.getUsername());
        user.setDeviceId(request.getDevice_id());
        Long userId = userService.join(user);
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", userId);
        return response;
    }


//    @GetMapping("/login")
//    @ResponseBody
//    public  Map<String, Long> login(@RequestBody LoginRequest loginRequest){
//        Map<String, Long> response = new HashMap<>();
//        Long deviceId = userService.login(loginRequest.getDevice_id());
//        response.put("loginResult", deviceId);
//        return response;
//    }
    @GetMapping("/login")
    public ResponseEntity<TokenDto> newLogin(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.newLogin(loginRequest.getDevice_id()));
    }
    @PutMapping("/rename")
    public ResponseEntity<String> changeUserName(
            @RequestParam("user_name") String userName,
            @RequestParam("user_id") Long userId) {
        try {
            if (userName != null && !userName.trim().isEmpty()) {
                Optional<User> user = userRepository.findById(userId);
                User real_user = user.get();
                userService.updateUserName(real_user, userName);
                return ResponseEntity.ok(userName);

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 이름은 공백일 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("업데이트 작업에 실패했습니다.");
        }
    }

//    User user = new User();
//                userService.updateUserName(user, userName);
//                return ResponseEntity.ok(userName);


}
