package com.hotelmanager.controller;

import com.hotelmanager.exception.RegraDeNegocioException;
import com.hotelmanager.model.Hospede;
import com.hotelmanager.service.HospedeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hospedes")
public class HospedeWebController {

    private final HospedeService hospedeService;

    public HospedeWebController(HospedeService hospedeService) {
        this.hospedeService = hospedeService;
    }

    @GetMapping
    public String listar(Model model) {
        prepararModelo(model, new Hospede());
        return "hospedes";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        prepararModelo(model, hospedeService.buscarPorId(id));
        return "hospedes";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("hospede") Hospede hospede, BindingResult result, Model model) {
        if (result.hasErrors()) {
            prepararModelo(model, hospede);
            return "hospedes";
        }

        try {
            if (hospede.getId() == null) {
                hospedeService.cadastrar(hospede);
            } else {
                hospedeService.atualizar(hospede.getId(), hospede);
            }
        } catch (RegraDeNegocioException exception) {
            model.addAttribute("mensagemAviso", exception.getMessage());
            prepararModelo(model, hospede);
            return "hospedes";
        }

        return "redirect:/hospedes";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            hospedeService.inativar(id);
        } catch (RegraDeNegocioException exception) {
            redirectAttributes.addFlashAttribute("mensagemAviso", exception.getMessage());
        }

        return "redirect:/hospedes";
    }

    private void prepararModelo(Model model, Hospede hospede) {
        model.addAttribute("hospede", hospede);
        model.addAttribute("hospedes", hospedeService.listarTodos());
    }
}
