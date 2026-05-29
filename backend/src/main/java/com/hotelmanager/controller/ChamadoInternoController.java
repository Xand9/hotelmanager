package com.hotelmanager.controller;

import com.hotelmanager.dto.ChamadoInternoFormDTO;
import com.hotelmanager.enums.StatusChamado;
import com.hotelmanager.enums.TipoChamado;
import com.hotelmanager.model.ChamadoInterno;
import com.hotelmanager.service.ChamadoInternoService;
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
@RequestMapping("/chamados")// Todas as rotas começam com /chamados
public class ChamadoInternoController {

    private final ChamadoInternoService chamadoInternoService;
    private final QuartoService quartoService;

    public ChamadoInternoController(ChamadoInternoService chamadoInternoService, QuartoService quartoService) {
        this.chamadoInternoService = chamadoInternoService;
        this.quartoService = quartoService;
    }

    @GetMapping
    public String listar(Model model) {
        prepararModelo(model, chamadoInternoService.criarFormulario());
        return "chamados";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        ChamadoInterno chamado = chamadoInternoService.buscarPorId(id);
        prepararModelo(model, chamadoInternoService.criarFormulario(chamado));
        return "chamados";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("chamadoForm") ChamadoInternoFormDTO form,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            prepararModelo(model, form);
            return "chamados";
        }

        chamadoInternoService.salvar(form);
        return "redirect:/chamados";
    }

    @PostMapping("/{id}/status/{status}")
    public String alterarStatus(@PathVariable Long id, @PathVariable StatusChamado status) {
        chamadoInternoService.alterarStatus(id, status);
        return "redirect:/chamados";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        chamadoInternoService.excluir(id);
        return "redirect:/chamados";
    }

    private void prepararModelo(Model model, ChamadoInternoFormDTO form) {
        model.addAttribute("chamadoForm", form);
        model.addAttribute("chamados", chamadoInternoService.listarTodos());
        model.addAttribute("quartos", quartoService.listarTodos());
        model.addAttribute("tipos", TipoChamado.values());
        model.addAttribute("statusChamados", StatusChamado.values());
    }
}
