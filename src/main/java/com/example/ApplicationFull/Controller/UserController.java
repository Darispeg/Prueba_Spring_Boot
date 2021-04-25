package com.example.ApplicationFull.Controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.ApplicationFull.Entity.User;
import com.example.ApplicationFull.Exception.UsernameOrIdNotFound;
import com.example.ApplicationFull.Repository.RoleRepository;
import com.example.ApplicationFull.Service.UserService;
import com.example.ApplicationFull.dto.ChangePasswordForm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    
	@Autowired
    @Qualifier("UserService")
	UserService uService;

    @Autowired
    RoleRepository repository;

    @GetMapping(value = {"/","/login"})
    public String index(){
        return "index";
    }

    @GetMapping("/userForm")
    public String userForm(Model model){
        model.addAttribute("userForm", new User());
        model.addAttribute("userList", uService.getAllUsers());
        model.addAttribute("roles", repository.findAll());
        model.addAttribute("listTab", "active");
        return "user-form/user-view";
    }

    @PostMapping("/userForm")
    public String createUser(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model){
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab", "active");
        }else{
            try {
                uService.createUser(user);
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab", "active");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", uService.getAllUsers());
                model.addAttribute("roles", repository.findAll());
            }
        }
        model.addAttribute("userList", uService.getAllUsers());
        model.addAttribute("roles", repository.findAll());
        return "user-form/user-view";
    }

    @GetMapping("/editUser/{id}")
    public String getEditUserForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
        User userToEdit = uService.getUserById(id);
        model.addAttribute("userForm", userToEdit);
        model.addAttribute("userList", uService.getAllUsers());
        model.addAttribute("roles", repository.findAll());
        model.addAttribute("formTab", "active");
    
        model.addAttribute("editMode", "true");

        model.addAttribute("passwordForm", new ChangePasswordForm(id));

        return "user-form/user-view";
    }

    Log log = LogFactory.getLog(getClass());

    @PostMapping("/editUser")
	public String postEditUserForm(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab","active");
			model.addAttribute("editMode","true");
            model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
            log.info("Llego hasta el error");
		}else {
			try {
				uService.updateUser(user);
				model.addAttribute("userForm", new User());
				model.addAttribute("listTab","active");
                log.info("Llego hasta el update");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage",e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab","active");
				model.addAttribute("userList", uService.getAllUsers());
				model.addAttribute("roles",repository.findAll());
				model.addAttribute("editMode","true");
                model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
			}
		}

		model.addAttribute("userList", uService.getAllUsers());
		model.addAttribute("roles",repository.findAll());
		return "user-form/user-view";

	}

    @PostMapping("/editUser/changePassword")
	public ResponseEntity<String> postEditUseChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {
		try {
			if( errors.hasErrors()) {
				String result = errors.getAllErrors()
                        .stream().map(x -> x.getDefaultMessage())
                        .collect(Collectors.joining(""));

				throw new Exception(result);
			}
			uService.changePassword(form);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Success");
	}

    @GetMapping("/userForm/cancel")
    public String cancelEditUser(ModelMap model){
        return "redirect:/userForm";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(Model model, @PathVariable(name = "id")Long id){
        try {
            uService.deleteUser(id);
        } catch (UsernameOrIdNotFound e) {
            model.addAttribute("listErrorMessage", e.getMessage());
        }
        return userForm(model);
    }
}
