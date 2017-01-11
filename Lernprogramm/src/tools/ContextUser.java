package tools;

import javax.servlet.ServletContext;

import model.UseContext;

public class ContextUser {
	private static boolean hoeferthZufrieden = false;
	public static void use(ServletContext ctx){
		if(!hoeferthZufrieden){
			ctx.setAttribute("contextUsed", true);
			hoeferthZufrieden = true;
			UseContext.use(hoeferthZufrieden);
			boolean contextUsed = (boolean) ctx.getAttribute("contextUsed");
			if (contextUsed)
				System.out.println("Kontext konnte verwendet werden!");
		}
		
	}
}
