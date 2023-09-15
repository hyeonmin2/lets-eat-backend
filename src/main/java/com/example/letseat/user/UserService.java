package com.example.letseat.user;

import com.example.letseat.plan.Plan;
import com.example.letseat.user.data.ListResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<ListResponse> findPlanByUserId(Long id) {
        User findUser = userRepository.findById(id).orElseThrow();
        List<Plan> planList = findUser.getPlans();
        List<ListResponse> listResponses = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(Plan plan : planList) {
            String name;
            if (!id.equals(plan.getUsers().get(0).getId()))
                name = plan.getUsers().get(0).getName();
            else if(!id.equals(plan.getUsers().get(1).getId()))
                name = plan.getUsers().get(1).getName();
            else throw new IllegalArgumentException("Plan doesn't have other user");
            ListResponse listResponse = ListResponse.builder()
                    .plan_id(plan.getId())
                    .expiration_date(plan.getExpiration_date().format(formatter))
                    .creation_date(plan.getCreation_date().format(formatter))
                    .other_user_name(name)
                    .build();
            listResponses.add(listResponse);
        }
        return listResponses;

    }

    @Transactional
    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }
//    public Optional<com.example.letseat.user.User> findById(Long id) {
//        return userRepository.findById(id);
//    }
    public Long login(String device_id) {
        Optional<User> findMember = userRepository.findByDeviceId(device_id);
        if (findMember.isPresent()) {
            User user = findMember.get();
            return user.getId();
        } else {
            return -1L;
        }

    }
}
