package com.abc.termproject.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.abc.termproject.entity.InvoiceItem;
import com.abc.termproject.utils.DatabaseUtility;

@Controller
//@RestController
public class NavigationController {
	
	DatabaseUtility dbUtil = new DatabaseUtility();

	// This may not be necessary until if/when we want a custom login page (this includes the login.jsp)
//	@GetMapping("/login")
//	public String login() {
//		return "login";
//	}
	
	// Used to redirect user based on login credentials
	@GetMapping("/")
	public String redirect(HttpServletRequest request) {
		if (request.isUserInRole("DRIVER")) {
			return "driver";
		}
		if (request.isUserInRole("ADMIN")) {
			return "admin";
		}
		return "customer";
	}
	
	// Used to access the admin page manually
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
	// Post request method for adding invoice items to DB
	@PostMapping(path="/admin")
	public void upload(@RequestParam int invoiceId, @RequestParam int userId, @RequestParam String date, @RequestParam int prodId, @RequestParam int quantity) {
		dbUtil.insertInvoice(invoiceId, userId, date, prodId, quantity);
	}
	
	// Used to access the admin page manually
	@GetMapping("/driver")
	public String driver() {
		return "driver";
	}
	
	// Post request method for changing the status of a delivery
    @PostMapping(path="/driver")
    public void driverButtonAction(@RequestParam String command) {
        
        int deliveryID;
        
        //System.out.println(command);
        //System.out.println(command.substring(0, 6));
        
        if (command.substring(0, 6).equals("Verify")) {
            
            try {
                
                deliveryID = Integer.parseInt(command.substring(7));
                
                //System.out.println(deliveryID);
                
                dbUtil.verifyDelivery(deliveryID);
                
                System.out.println("DeliveryID " + deliveryID + " was verified");
                
            } catch (Exception ex) {
                
                System.out.println("error - could not verify delivery\n" + ex.getMessage());
                
            }
            
        } else if (command.substring(0, 6).equals("Cancel")) {
            
            try {
                
                deliveryID = Integer.parseInt(command.substring(7));
                
                //System.out.println(deliveryID);
                
                dbUtil.cancelDelivery(deliveryID);
                
                System.out.println("DeliveryID " + deliveryID + " was cancelled");
                
            } catch (Exception ex) {
                
                System.out.println("error - could not cancel delivery\n" + ex.getMessage());
                
            }
        }
    }
	
	// Used to access the customer page manually
	@GetMapping("/customer")
	public String customer() {
		return "customer";
	}
	
	public String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    return currentUserName;
		}
		return null;
	}
}
