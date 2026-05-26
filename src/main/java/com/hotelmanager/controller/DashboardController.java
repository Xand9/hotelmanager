package com.hotelmanager.controller;

import com.hotelmanager.enums.StatusChamado;
import com.hotelmanager.enums.StatusQuarto;
import com.hotelmanager.enums.StatusReserva;
import com.hotelmanager.repository.ChamadoInternoRepository;
import com.hotelmanager.repository.QuartoRepository;
import com.hotelmanager.repository.ReservaRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final QuartoRepository quartoRepository;
    private final ReservaRepository reservaRepository;
    private final ChamadoInternoRepository chamadoInternoRepository;

    public DashboardController(
            QuartoRepository quartoRepository,
            ReservaRepository reservaRepository,
            ChamadoInternoRepository chamadoInternoRepository
    ) {
        this.quartoRepository = quartoRepository;
        this.reservaRepository = reservaRepository;
        this.chamadoInternoRepository = chamadoInternoRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        LocalDate hoje = LocalDate.now();

        model.addAttribute("totalQuartos", quartoRepository.countByAtivoTrue());
        model.addAttribute("quartosDisponiveis", quartoRepository.countByAtivoTrueAndStatus(StatusQuarto.DISPONIVEL));
        model.addAttribute("quartosOcupados", quartoRepository.countByAtivoTrueAndStatus(StatusQuarto.OCUPADO));
        model.addAttribute("quartosLimpeza", quartoRepository.countByAtivoTrueAndStatus(StatusQuarto.EM_LIMPEZA));
        model.addAttribute("quartosManutencao", quartoRepository.countByAtivoTrueAndStatus(StatusQuarto.EM_MANUTENCAO));
        model.addAttribute("reservasAtivas", reservaRepository.countByStatus(StatusReserva.RESERVADA));
        model.addAttribute("checkinsHoje", reservaRepository.countByStatusAndDataEntrada(StatusReserva.CHECKIN_REALIZADO, hoje));
        model.addAttribute("checkoutsHoje", reservaRepository.countByStatusAndDataSaida(StatusReserva.CHECKOUT_REALIZADO, hoje));
        model.addAttribute("chamadosAbertos", chamadoInternoRepository.countByStatus(StatusChamado.ABERTO));

        return "dashboard";
    }
}
