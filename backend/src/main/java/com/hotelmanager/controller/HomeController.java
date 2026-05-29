package com.hotelmanager.controller;
//definir o que acontece quando o usuário entra na raiz do sistema
import org.springframework.stereotype.Controller;//Essa classe é um controller web.
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")//Quando alguém acessar /, execute o método abaixo.
    public String inicio() {
        return "redirect:/dashboard";
    }
}
//Ele recebe o acesso à rota principal / e redireciona para /dashboard.

