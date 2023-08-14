package com.example.ValidatorService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/v1/validator")
public class MainServiceController {
    private final RestTemplate restTemplate;

    public MainServiceController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/ValidateLines")
    public String getApi(@RequestBody()String url){
        String fileName= url;
        String fe = "";
        String response = null;
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            fe = fileName.substring(i+1);
        }

        System.out.println(fe);

        if( fe.equals("csv")){
            System.out.println("prueba");
            String urls = "http://localhost:8080/api/v1/pe/CSV";
            response =  restTemplate.postForEntity(urls,url, String.class).getBody();
        }else if(fe.equals("xlsx")){
            String urls = "http://localhost:8080/api/v1/pe/EXCEL";
            response = restTemplate.postForEntity(urls,url, String.class).getBody();
        }
        return response;
    }
}