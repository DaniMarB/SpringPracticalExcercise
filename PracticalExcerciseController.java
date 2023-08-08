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
    public String uploadCSVList(){
        return this.service.uploadCSVList();
    }

    @PostMapping("/EXCEL")
    public String uploadxlsxList() throws IOException {
        return this.service.uploadxlsxList();
    }
}
