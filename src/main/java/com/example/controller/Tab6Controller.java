package com.example.controller;

import com.example.entity.Label;
import com.example.service.LabelService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class Tab6Controller {

    @Autowired
    LabelService labelService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String create(@RequestBody Label label) {
        labelService.save(label);
        return "Label saved successfully!";
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String list() {
        List<Label> labels = labelService.list();
        JSONObject o = new JSONObject();
        o.put("labels", new JSONArray(labels));
        return o.toString();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String delete(@PathVariable int id) {
        labelService.delete(id);
        return "Label deleted!";
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String update(@RequestBody Label label) {
        labelService.save(label);
        return "Label updated!";
    }
}
