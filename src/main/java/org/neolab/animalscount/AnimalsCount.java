package org.neolab.animalscount;

import org.neolab.animalscount.service.AnimalsCountService;

import java.io.File;

public class AnimalsCount {


    public static void main(String[] arg) {
        new AnimalsCountService().animalsCountTask(new File("animals_list.csv"), new File("filter_list.afl"));
    }
}
