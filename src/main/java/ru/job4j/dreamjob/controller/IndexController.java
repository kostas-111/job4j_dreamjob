package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для обработки запросов к индексной странице.
 * Потокобезопасен, так как не содержит состояния.
 */
@ThreadSafe
@Controller
@SuppressWarnings("unused")
public class IndexController {
    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }
}