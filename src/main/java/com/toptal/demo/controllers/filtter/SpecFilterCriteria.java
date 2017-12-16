package com.toptal.demo.controllers.filtter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecFilterCriteria {
    private String key;
    private RsqlSearchOperation operation;
    private Object value;
}
