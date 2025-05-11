package ru.job4j.dreamjob.controller;

import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.dreamjob.model.User;

/**
 * Контроллер для обработки запросов к индексной странице.
 * Потокобезопасен, так как не содержит состояния.
 */
@ThreadSafe
@Controller
@SuppressWarnings("unused")
public class IndexController {

    @GetMapping({"/", "/index"})
    public String getIndex() {
        return "index";
    }
}