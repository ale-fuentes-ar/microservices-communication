package ar.fuentes.ale.productapi.modules.status.controle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/")
public class StatusController {


    @GetMapping
    public ResponseEntity<HashMap<String, Object>> getRootStatus(){
        return ResponseEntity.ok(getSuccessResponse());
    }

    @GetMapping("api/status")
    public ResponseEntity<HashMap<String, Object>> getStatus(){
        return ResponseEntity.ok(getSuccessResponse());
    }

    private HashMap<String, Object> getSuccessResponse() {
        var response = new HashMap<String, Object>();

        response.put("Service", "Product API");
        response.put("httpStatus", HttpStatus.OK.value());
        response.put("Status", "UP");

        return response;
    }
}
