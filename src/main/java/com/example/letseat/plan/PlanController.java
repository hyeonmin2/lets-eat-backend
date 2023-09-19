package com.example.letseat.plan;

import com.example.letseat.plan.data.PlanRequest;
import com.example.letseat.plan.data.QrRequest;
import com.example.letseat.plan.data.QrResponse;
import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@AllArgsConstructor
public class PlanController {
    private final PlanService planService;

    @PostMapping("/qr")
    @ResponseBody
    public ResponseEntity<QrResponse> lists(@RequestBody @Valid QrRequest qrRequest) throws IOException, WriterException {

        QrResponse qrcode = planService.generateQR(qrRequest);

        return ResponseEntity.ok(qrcode);
    }

    @PostMapping("/plan")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void plan(@RequestBody @Valid PlanRequest planRequest) {
        Long senderId = planRequest.getSender_id();
        Long receiverId = planRequest.getReceiver_id();
        LocalDate expiredDate = planRequest.getExpired_date();
        planService.savePlan(senderId, receiverId, expiredDate);
    }

    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<Object> test() {
        class TestResponse {
            public String connect;
        }
        TestResponse testResponse = new TestResponse();
        testResponse.connect = "OK";
        return ResponseEntity.ok(testResponse);
    }
}
