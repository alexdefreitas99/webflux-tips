package com.alex.springtips;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MinhaModelDeTeste {
    private String suid;
    private String name;

    MinhaModelDeTeste(String name) {
        this.name = name;
    }

}
