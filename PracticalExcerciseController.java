package com.example.PracticalExcercise;

import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController()
@RequestMapping("api/v1/pe")
public class PracticalExcerciseController {

    PracticalExcerciseService service = new PracticalExcerciseService();


    @GetMapping()
    public ArrayList<People> printPeopleList(){
        return this.service.printPeopleList();
    }
    @PostMapping("/CSV")
    public String uploadCSVList(@RequestBody()String url){
        return this.service.uploadCSVList(url);
    }

    @PostMapping("/EXCEL")
    public String uploadxlsxList(@RequestBody() String url) throws IOException {
        return this.service.uploadxlsxList(url);
    }
}
