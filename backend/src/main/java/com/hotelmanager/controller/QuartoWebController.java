package com.hotelmanager.controller;

import com.hotelmanager.enums.StatusQuarto;
import com.hotelmanager.enums.TipoQuarto;
import com.hotelmanager.model.Quarto;
import com.hotelmanager.service.QuartoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quartos")
public class QuartoWebController {

    private final QuartoService quartoService;

    public QuartoWebController(QuartoService quartoService) {
        this.quartoService = quartoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("quartos", quartoService.listarTodos());
        return "quartos";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        prepararFormulario(model, new Quarto());
        return "cadastro-quarto";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        prepararFormulario(model, quartoService.buscarPorId(id));
        return "cadastro-quarto";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("quarto") Quarto quarto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            prepararFormulario(model, quarto);
            return "cadastro-quarto";
        }

        if (quarto.getId() == null) {
            quartoService.cadastrar(quarto);
        } else {
            quartoService.atualizar(quarto.getId(), quarto);
        }

        return "redirect:/quartos";
    }

    @PostMapping("/{id}/status/{status}")
    public String alterarStatus(@PathVariable Long id, @PathVariable StatusQuarto status) {
        quartoService.alterarStatus(id, status);
        return "redirect:/quartos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        quartoService.inativar(id);
        return "redirect:/quartos";
    }

    private void prepararFormulario(Model model, Quarto quarto) {
        model.addAttribute("quarto", quarto);
        model.addAttribute("tipos", TipoQuarto.values());
        model.addAttribute("statusQuartos", StatusQuarto.values());
    }
}
