package com.hotelmanager.controller;

import com.hotelmanager.dto.ReservaFormDTO;
import com.hotelmanager.dto.ReservaRequestDTO;
import com.hotelmanager.exception.RegraDeNegocioException;
import com.hotelmanager.model.Reserva;
import com.hotelmanager.service.HospedeService;
import com.hotelmanager.service.QuartoService;
import com.hotelmanager.service.ReservaService;
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
@RequestMapping("/reservas")
public class ReservaWebController {

    private final ReservaService reservaService;
    private final HospedeService hospedeService;
    private final QuartoService quartoService;

    public ReservaWebController(
            ReservaService reservaService,
            HospedeService hospedeService,
            QuartoService quartoService
    ) {
        this.reservaService = reservaService;
        this.hospedeService = hospedeService;
        this.quartoService = quartoService;
    }

    @GetMapping
    public String listar(Model model) {
        prepararModelo(model, new ReservaFormDTO(null, null, null, null, null, null));
        return "reservas";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Reserva reserva = reservaService.buscarPorId(id);
        ReservaFormDTO form = new ReservaFormDTO(
                reserva.getId(),
                reserva.getHospede().getId(),
                reserva.getQuarto().getId(),
                reserva.getDataEntrada(),
                reserva.getDataSaida(),
                reserva.getObservacoes()
        );
        prepararModelo(model, form);
        return "reservas";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("reservaForm") ReservaFormDTO form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("mensagemErro", "Confira os dados obrigatorios da reserva.");
            prepararModelo(model, form);
            return "reservas";
        }

        ReservaRequestDTO dto = new ReservaRequestDTO(
                form.hospedeId(),
                form.quartoId(),
                form.dataEntrada(),
                form.dataSaida(),
                form.observacoes()
        );

        try {
            if (form.id() == null) {
                reservaService.cadastrarReserva(dto);
            } else {
                reservaService.atualizar(form.id(), dto);
            }
        } catch (RegraDeNegocioException exception) {
            model.addAttribute("mensagemErro", exception.getMessage());
            prepararModelo(model, form);
            return "reservas";
        }

        return "redirect:/reservas";
    }

    @PostMapping("/{id}/checkin")
    public String realizarCheckin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaService.realizarCheckin(id);
        } catch (RegraDeNegocioException exception) {
            redirectAttributes.addFlashAttribute("mensagemErro", exception.getMessage());
        }
        return "redirect:/reservas";
    }

    @PostMapping("/{id}/checkout")
    public String realizarCheckout(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaService.realizarCheckout(id);
        } catch (RegraDeNegocioException exception) {
            redirectAttributes.addFlashAttribute("mensagemErro", exception.getMessage());
        }
        return "redirect:/reservas";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaService.cancelar(id);
        } catch (RegraDeNegocioException exception) {
            redirectAttributes.addFlashAttribute("mensagemErro", exception.getMessage());
        }
        return "redirect:/reservas";
    }

    private void prepararModelo(Model model, ReservaFormDTO form) {
        model.addAttribute("reservaForm", form);
        model.addAttribute("reservas", reservaService.listarTodas());
        model.addAttribute("hospedes", hospedeService.listarTodos());
        model.addAttribute("quartos", quartoService.listarTodos());
    }
}
