package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.Administrador;
import com.soulcode.goserviceapp.domain.Servico;
import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.domain.UsuarioLog;
import com.soulcode.goserviceapp.service.AdminService;
import com.soulcode.goserviceapp.service.ServicoService;
import com.soulcode.goserviceapp.service.UsuarioLogService;
import com.soulcode.goserviceapp.service.UsuarioService;
import com.soulcode.goserviceapp.service.exceptions.ServicoNaoEncontradoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdministradorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private UsuarioLogService usuarioLogService;

    @Autowired
    private AdminService adminService;

    @GetMapping(value = "/servicos")
    public ModelAndView servico(@RequestParam(name = "page", defaultValue = "1") int page){
        ModelAndView mv = new ModelAndView("servicosAdmin");
        try{
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<Servico> servicosPage = servicoService.findServicoByPage(pageable);
            List<Servico> servicos = servicosPage.getContent();
            long totalServicos = servicosPage.getTotalElements();
            int totalPages = servicosPage.getTotalPages();
            mv.addObject("servicos", servicos);
            mv.addObject("currentPage", page);
            mv.addObject("totalPages", totalPages);
        }   catch (ServicoNaoEncontradoException ex) {
            mv.addObject("errorMessage", "Erro ao encontrar dados de serviço.");
        }
        return mv;
    }

    @PostMapping(value = "/servicos")
    public String createService(Servico servico, RedirectAttributes attributes) {
        try {
            servicoService.createServico(servico);
            attributes.addFlashAttribute("successMessage", "Novo serviço adicionado.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao adicionar novo serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @PostMapping(value = "/servicos/remover")
    public String removeService(@RequestParam(name = "servicoId") Long id, RedirectAttributes attributes) {
        try {
            servicoService.removeServicoById(id);
            attributes.addFlashAttribute("successMessage", "Serviço removido.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao excluir serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @GetMapping(value = "/servicos/editar/{id}")
    public ModelAndView editService(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("editarServico");
        try {
            Servico servico = servicoService.findById(id);
            mv.addObject("servico", servico);
        } catch (ServicoNaoEncontradoException ex) {
            mv.addObject("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados do serviço.");
        }
        return mv;
    }

    @PostMapping(value = "/servicos/editar")
    public String updateService(Servico servico, RedirectAttributes attributes) {
        try {
            servicoService.update(servico);
            attributes.addFlashAttribute("successMessage", "Dados do serviço alterados.");
        } catch (ServicoNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao alterar dados do serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @GetMapping(value = "/usuarios")
    public ModelAndView usuarios(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "filtro", required = false) String filtro) {
        ModelAndView mv = new ModelAndView("usuariosAdmin");

        try {
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<Usuario> usuariosPage;

            if (filtro != null && !filtro.isEmpty()) {
                usuariosPage = usuarioService.findUsersByNameWithPage(filtro, pageable);
            } else {
                usuariosPage = usuarioService.findUsersByPage(pageable);
            }

            List<Usuario> usuarios = usuariosPage.getContent();
            long totalUsuarios = usuariosPage.getTotalElements();
            int totalPages = usuariosPage.getTotalPages();

            mv.addObject("usuarios", usuarios);
            mv.addObject("currentPage", page);
            mv.addObject("totalPages", totalPages);
            mv.addObject("filtro", filtro);

        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados de usuários.");
        }

        return mv;
    }

    @PostMapping(value = "/usuarios")
    public String createUser(Usuario usuario, RedirectAttributes attributes) {
        try {
            usuarioService.createUser(usuario);
            attributes.addFlashAttribute("successMessage", "Novo usuário cadastrado.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao cadastrar novo usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping(value = "/usuarios/disable")
    public String disableUser(@RequestParam(name = "usuarioId") Long id, RedirectAttributes attributes) {
        try {
            usuarioService.disableUser(id);
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao desabilitar usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping(value = "/usuarios/enable")
    public String enableUser(@RequestParam(name = "usuarioId") Long id, RedirectAttributes attributes) {
        try {
            usuarioService.enableUser(id);
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao habilitar usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping(value = "/dashboard")
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView("dashboard");
        try {
            List<UsuarioLog> logsAuth = usuarioLogService.findAll();
            mv.addObject("logsAuth", logsAuth);
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados de log de autenticação.");
        }
        return mv;
    }
    @GetMapping(value = "/dados")
    public ModelAndView dados(Authentication authentication) {
        ModelAndView mv = new ModelAndView("dadosAdministrador");
        try {
            Administrador admin = adminService.findAuthenticated(authentication);
            mv.addObject("administrador", admin);
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ex) {
            mv.addObject("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados do Administrador.");
        }
        return mv;
    }

    @PostMapping(value = "/dados")
    public String alterarDados(Administrador administrador, RedirectAttributes attributes) {
        try {
            adminService.update(administrador);
            attributes.addFlashAttribute("successMessage", "Dados alterados.");
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao alterar dados cadastrais.");
        }
        return "redirect:/admin/dados";
    }

        @GetMapping
        public String listarServicos(Model model, @RequestParam(name = "filtroNome", required = false) String filtroNome) {
            List<Servico> servicos;

            if (filtroNome != null && !filtroNome.isEmpty()) {
                servicos = servicoService.findByNomeContaining(filtroNome);
            } else {
                servicos = servicoService.findAll();
            }
            model.addAttribute("servicos", servicos);
            return "admin/lista-servicos";
        }
}