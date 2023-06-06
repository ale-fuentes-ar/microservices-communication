package ar.fuentes.ale.productapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping()
    public ResponseEntity<HashMap<String, Object>> getStatus(){
        var response = new HashMap<String, Object>();

        response.put("Service", "Product API");
        response.put("httpStatus", HttpStatus.OK.value());
        response.put("Status", "UP");

        return ResponseEntity.ok(response);
    }
}
