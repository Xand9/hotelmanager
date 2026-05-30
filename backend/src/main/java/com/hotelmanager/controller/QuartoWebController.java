package com.hotelmanager.controller;

import com.hotelmanager.enums.StatusQuarto;
import com.hotelmanager.enums.TipoQuarto;

import com.hotelmanager.exception.RegraDeNegocioException;
import com.hotelmanager.model.Quarto;
import com.hotelmanager.service.LimpezaDiariaService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/quartos")
public class QuartoWebController {//Recebe pedidos HTML chama QuartoService devolve tela

    private final QuartoService quartoService;
    private final LimpezaDiariaService limpezaDiariaService;

    public QuartoWebController(QuartoService quartoService, LimpezaDiariaService limpezaDiariaService) {
        this.quartoService = quartoService;
        this.limpezaDiariaService = limpezaDiariaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("quartos", quartoService.listarTodos());
        model.addAttribute("reservasAtivasPorQuarto", quartoService.buscarReservasAtivasPorQuarto());
        model.addAttribute("chamadosAtivosPorQuarto", quartoService.buscarChamadosAtivosPorQuarto());
        model.addAttribute("limpezasDiariasPorQuarto", limpezaDiariaService.buscarLimpezasDeHojePorQuarto());
        return "quartos";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        Quarto quarto = new Quarto();
        quarto.setStatus(StatusQuarto.DISPONIVEL);
        prepararFormulario(model, quarto);
        return "cadastro-quarto";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            prepararFormulario(model, quartoService.buscarParaEdicao(id));
        } catch (RegraDeNegocioException exception) {
            redirectAttributes.addFlashAttribute("mensagemAviso", exception.getMessage());
            return "redirect:/quartos";
        }

        return "cadastro-quarto";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("quarto") Quarto quarto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            prepararFormulario(model, quarto);
            return "cadastro-quarto";
        }

        try {
            if (quarto.getId() == null) {
                quartoService.cadastrar(quarto);
            } else {
                quartoService.atualizar(quarto.getId(), quarto);
            }
        } catch (RegraDeNegocioException exception) {
            model.addAttribute("mensagemAviso", exception.getMessage());
            prepararFormulario(model, quarto);
            return "cadastro-quarto";
        }

        return "redirect:/quartos";
    }

    @PostMapping("/{id}/status/{status}")
    public String alterarStatus(
            @PathVariable Long id,
            @PathVariable StatusQuarto status,
            RedirectAttributes redirectAttributes
    ) {
        try {
            quartoService.alterarStatus(id, status);
        } catch (RegraDeNegocioException exception) {
            redirectAttributes.addFlashAttribute("mensagemAviso", exception.getMessage());
        }

        return "redirect:/quartos";
    }

    @PostMapping("/{quartoId}/limpeza-diaria/{reservaId}/realizar")
    public String marcarLimpezaDiaria(
            @PathVariable Long quartoId,
            @PathVariable Long reservaId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            limpezaDiariaService.marcarLimpezaDeHojeComoRealizada(quartoId, reservaId);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Limpeza diaria marcada como realizada.");
        } catch (RegraDeNegocioException exception) {
            redirectAttributes.addFlashAttribute("mensagemAviso", exception.getMessage());
        }

        return "redirect:/quartos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            quartoService.inativar(id);
        } catch (RegraDeNegocioException exception) {
            redirectAttributes.addFlashAttribute("mensagemAviso", exception.getMessage());
        }

        return "redirect:/quartos";
    }

    private void prepararFormulario(Model model, Quarto quarto) {
        model.addAttribute("quarto", quarto);
        model.addAttribute("tipos", TipoQuarto.values());
        model.addAttribute("statusQuartos", StatusQuarto.values());
    }
}
