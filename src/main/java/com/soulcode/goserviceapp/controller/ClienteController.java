package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.*;
import com.soulcode.goserviceapp.repository.EnderecoRepository;
import com.soulcode.goserviceapp.service.*;
import com.soulcode.goserviceapp.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping(value = "/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private PrestadorService prestadorService;

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping(value = "/dados")
    public ModelAndView dados(Authentication authentication) {
        ModelAndView mv = new ModelAndView("dadosCliente");
        try {
            Cliente cliente = clienteService.findAuthenticated(authentication);
            mv.addObject("cliente", cliente);
            mv.addObject("endereco", cliente.getEndereco());
            System.err.println(cliente.getEndereco());
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ex) {
            mv.addObject("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados do cliente.");
        }
        return mv;
    }

    @PostMapping(value = "/dados")
    public String alterarDados(
            Cliente cliente,
            RedirectAttributes attributes) {
        try {
            clienteService.update(cliente);
            attributes.addFlashAttribute("successMessage", "Dados alterados.");
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao alterar dados cadastrais.");
        }
        return "redirect:/cliente/dados";
    }

    @PostMapping(value = "/dados/endereco")
    public String alterarEndereco(
            Endereco endereco,
            RedirectAttributes attributes) {
        try {
            enderecoService.updateEndereco(endereco);
            attributes.addFlashAttribute("successMessage", "Dados alterados.");
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao alterar dados cadastrais.");
        }
        return "redirect:/cliente/dados";
    }

    @GetMapping(value = "/agendar")
    public ModelAndView agendar(@RequestParam(name = "especialidade", required = false) Long servicoId) {
        ModelAndView mv = new ModelAndView("agendarServico");
        try {
            List<Servico> servicos = servicoService.findAll();
            mv.addObject("servicos", servicos);
            if (servicoId != null) {
                List<Prestador> prestadores = prestadorService.findByServicoId(servicoId);
                mv.addObject("prestadores", prestadores);
                mv.addObject("servicoId", servicoId);
            }
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados de serviços.");
        }
        return mv;
    }

    @PostMapping(value = "/agendar")
    public String criarAgendamento(
            @RequestParam(name = "servicoId") Long servicoId,
            @RequestParam(name = "prestadorId") Long prestadorId,
            @RequestParam(name = "data") LocalDate data,
            @RequestParam(name = "hora") LocalTime hora,
            Authentication authentication,
            RedirectAttributes attributes) {
        try {
            Prestador prestador = prestadorService.findById(prestadorId);
            if (!agendamentoService.isHorarioLivre(prestador, data, hora)) {
                throw new HorarioIndisponivelException("O Prestador já possui um agendamento nesse horario.");
            }
            agendamentoService.create(authentication, servicoId, prestadorId, data, hora);
            attributes.addFlashAttribute("successMessage", "Agendamento realizado com sucesso. Aguardando confirmação.");
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException | ServicoNaoEncontradoException | HorarioIndisponivelException ex){
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        }catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao finalizar agendamento.");
        }
        return "redirect:/cliente/historico";
    }

    @GetMapping(value = "/historico")
    public ModelAndView historico(Authentication authentication, @RequestParam(name = "page", defaultValue = "0") int page) {
        ModelAndView mv = new ModelAndView("historicoCliente");
        try {
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Agendamento> agendamentosPage = agendamentoService.findAgendamentoByPage(pageable);
            List<Agendamento> agendamentos = agendamentosPage.getContent();
            long totalAgendamentos = agendamentosPage.getTotalElements();
            int totalPages = agendamentosPage.getTotalPages();
            mv.addObject("agendamentos", agendamentos);
            mv.addObject("currentPage", page);
            mv.addObject("totalPages", totalPages);
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ex) {
            mv.addObject("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao carregar dados de agendamentos.");
        }
        return mv;
    }

    @PostMapping (value = "/historico/pesquisa")
    public ModelAndView searchAgenda(
            @RequestParam(name = "data-inicio")String dataInicio,
            @RequestParam(name = "data-fim")String dataFim){
        ModelAndView mv = new ModelAndView("historicoCliente");
        try {
            List<Agendamento> busca_agendamento = agendamentoService.findByData(dataInicio, dataFim);
            mv.addObject("agendamentos", busca_agendamento);
        }catch (AgendamentoNaoEncontradoException ex){
            mv.addObject("errorMessage", ex.getMessage());
        }catch (Exception ex){
            mv.addObject("errorMessage", "Erro ao buscar agendamento");
        }
        return mv;
    }


    @PostMapping(value = "/historico/cancelar")
    public String cancelarAgendamento(
            @RequestParam(name = "agendamentoId") Long agendamentoId,
            Authentication authentication,
            RedirectAttributes attributes) {
        try {
            agendamentoService.cancelAgendaCliente(authentication, agendamentoId);
            attributes.addFlashAttribute("successMessage", "Agendamento cancelado.");
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException |
                 AgendamentoNaoEncontradoException | StatusAgendamentoImutavelException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao cancelar agendamento.");
        }
        return "redirect:/cliente/historico";
    }

    @PostMapping(value = "/historico/concluir")
    public String concluirAgendamento(
            @RequestParam(name = "agendamentoId") Long agendamentoId,
            Authentication authentication,
            RedirectAttributes attributes) {
        try {
            agendamentoService.completeAgenda(authentication, agendamentoId);
            attributes.addFlashAttribute("successMessage", "Agendamento concluído.");
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException |
                 AgendamentoNaoEncontradoException | StatusAgendamentoImutavelException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao concluir agendamento.");
        }
        return "redirect:/cliente/historico";
    }

}
