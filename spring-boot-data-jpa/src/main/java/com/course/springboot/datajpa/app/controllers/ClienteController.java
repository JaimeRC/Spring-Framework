package com.course.springboot.datajpa.app.controllers;

import com.course.springboot.datajpa.app.models.entity.Cliente;
import com.course.springboot.datajpa.app.models.service.IClienteService;
import com.course.springboot.datajpa.app.utils.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageResquest = new PageRequest(page, 5);

        Page<Cliente> clientes = clienteService.findAll(pageResquest);

        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }


    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Clientes");

        return "form";

    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Cliente cliente = null;
        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "El ID del cliente no existe en la DB.");
                return "redict:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del cliente no puede ser 0.");
            return "redict:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");

        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Map<String, Object> model, RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.put("titulo", "Formulario de Clientes");
            return "form";
        }

        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", "Cliente creado con exito.");
        return "redirect:listar";
    }

    @RequestMapping(value = "/eliminar/{id}", method = RequestMethod.DELETE)
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        if (id > 0) {
            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exito.");

        }
        return "redirect:/listar";
    }

}
